package com.graduationProject._thYear.Purchase.services;


import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Account.repositories.AccountRepository;
import com.graduationProject._thYear.Auth.models.User;
import com.graduationProject._thYear.Auth.repositories.UserRepository;
import com.graduationProject._thYear.Currency.repositories.CurrencyRepository;
import com.graduationProject._thYear.Invoice.models.InvoiceHeader;
import com.graduationProject._thYear.Invoice.models.InvoiceItem;
import com.graduationProject._thYear.Invoice.models.InvoiceKind;
import com.graduationProject._thYear.Invoice.repositories.InvoiceHeaderRepository;
import com.graduationProject._thYear.InvoiceType.repositories.InvoiceTypeRepository;
import com.graduationProject._thYear.Journal.models.JournalHeader;
import com.graduationProject._thYear.Journal.models.JournalItem;
import com.graduationProject._thYear.Journal.models.JournalKind;
import com.graduationProject._thYear.Journal.repositories.JournalHeaderRepository;
import com.graduationProject._thYear.ProductStock.models.ProductStock;
import com.graduationProject._thYear.ProductStock.services.ProductStockService;
import com.graduationProject._thYear.Purchase.dtos.requests.CreatePurchaseHeaderRequest;
import com.graduationProject._thYear.Purchase.dtos.requests.UpdatePurchaseHeaderRequest;
import com.graduationProject._thYear.Purchase.dtos.responses.PurchaseHeaderResponse;
import com.graduationProject._thYear.Purchase.dtos.responses.PurchaseItemResponse;
import com.graduationProject._thYear.Purchase.models.PurchaseHeader;
import com.graduationProject._thYear.Purchase.models.PurchaseItem;
import com.graduationProject._thYear.Purchase.models.StatusType;
import com.graduationProject._thYear.Purchase.repositories.PurchaseHeaderRepository;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.repositories.ProductRepository;
import com.graduationProject._thYear.Purchase.repositories.PurchaseItemRepository;
import com.graduationProject._thYear.Transfer.models.Transfer;
import com.graduationProject._thYear.Transfer.models.TransferItem;
import com.graduationProject._thYear.Unit.models.UnitItem;
import com.graduationProject._thYear.Unit.repositories.UnitItemRepository;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import com.graduationProject._thYear.Warehouse.repositories.WarehouseRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseHeaderRepository purchaseHeaderRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final UnitItemRepository unitItemRepository;
    private final ProductStockService stockService;
    private final InvoiceTypeRepository invoiceTypeRepository;
    private final InvoiceHeaderRepository invoiceHeaderRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final AccountRepository accountRepository;
    private final JournalHeaderRepository journalHeaderRepository;
    @Transactional
    public PurchaseHeaderResponse create(CreatePurchaseHeaderRequest request) {
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Purchase must contain at least one item");
        }

        LocalDateTime supplyDate = request.getSupplyDate() != null
                ? request.getSupplyDate()
                : LocalDateTime.now();

        PurchaseHeader header = PurchaseHeader.builder()
                .WarehouseId(warehouse)
                .supplyDate(supplyDate)
                .requestDate(null)
                .buyDate(null)
                .receiveDate(null)
                .total(request.getTotal())
                .supplierName(request.getSupplierName())
                .notes(request.getNotes())
                .status(StatusType.supply) //  always start with "supply"
                .build();

        List<PurchaseItem> items = request.getItems().stream().map(itemReq -> {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            UnitItem unitItem = resolveUnitItem(product, itemReq.getUnitItemId());

            BigDecimal unitFact = itemReq.getUnitFact() != null
                    ? itemReq.getUnitFact()
                    : BigDecimal.valueOf(unitItem.getFact());

            return PurchaseItem.builder()
                    .purchaseHeader(header)
                    .productId(product)
                    .qty(itemReq.getQty())
                    .unitItemId(unitItem)
                    .unitFact(unitFact)
                    .build();
        }).collect(Collectors.toList());

        header.setItems(items);

        PurchaseHeader saved = purchaseHeaderRepository.save(header);

        return toResponse(saved);
    }

    @Transactional
    public PurchaseHeaderResponse update(Integer id, UpdatePurchaseHeaderRequest req) {
        PurchaseHeader header = purchaseHeaderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found"));

        StatusType oldStatus = header.getStatus();

        if(oldStatus == StatusType.receive){
            throw (new ResourceNotFoundException("can't update the purchase if status is receive "));
        }

        if (req.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(req.getWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
            header.setWarehouseId(warehouse);
        }

        if (req.getNotes() != null) {
            header.setNotes(req.getNotes());
        }

        if (req.getTotal() != null) {
            header.setTotal(req.getTotal());
        }

        if (req.getSupplierName() != null) {
            header.setSupplierName(req.getSupplierName());
        }


        if (req.getStatus() != null) {
            StatusType newStatus = StatusType.fromCode(req.getStatus());
            header.setStatus(newStatus);

            switch (newStatus) {
                case request -> header.setRequestDate(LocalDateTime.now());
                case buy -> header.setBuyDate(LocalDateTime.now());
                case receive -> header.setReceiveDate(LocalDateTime.now());
                case supply -> header.setSupplyDate(LocalDateTime.now());
            }
        }

        if (req.getItems() != null && !req.getItems().isEmpty()) {

            // Only rollback if it was already "receive" before
            if (oldStatus == StatusType.receive) {
                for (PurchaseItem oldItem : header.getItems()) {
                    stockService.decreaseStock(
                            oldItem.getProductId().getId(),
                            header.getWarehouseId().getId(),
                            oldItem.getUnitItemId().getId(),
                            oldItem.getQty()
                    );
                }
            }

            header.getItems().clear();

            List<PurchaseItem> newItems = req.getItems().stream().map(itemReq -> {
                Product product = productRepository.findById(itemReq.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

                UnitItem unitItem = resolveUnitItem(product, itemReq.getUnitItemId());

                BigDecimal unitFact = itemReq.getUnitFact() != null
                        ? itemReq.getUnitFact()
                        : BigDecimal.valueOf(unitItem.getFact());

                // If new status is receive → increase stock
                if (header.getStatus() == StatusType.receive) {
                    Optional<ProductStock> stock = stockService.findStock(
                            product.getId(),
                            header.getWarehouseId().getId(),
                            unitItem.getId()
                    );
                    if (stock.isEmpty()) {
                        stockService.createStock(product.getId(), header.getWarehouseId().getId(), unitItem.getId(), BigDecimal.ZERO);
                    }
                    stockService.increaseStock(product.getId(), header.getWarehouseId().getId(), unitItem.getId(), itemReq.getQty());
                }

                return PurchaseItem.builder()
                        .purchaseHeader(header)
                        .productId(product)
                        .qty(itemReq.getQty())
                        .unitItemId(unitItem)
                        .unitFact(unitFact)
                        .build();
            }).toList();

            header.getItems().addAll(newItems);
        }

        PurchaseHeader updated = purchaseHeaderRepository.save(header);

        if(header.getStatus() == StatusType.receive){
            createTransferInvoices(header);
                   deleteJournalForTransfer(header.getId());
                   createJournalForTransfer(header);
        }

        return toResponse(updated);
    }

    public List<PurchaseHeaderResponse> getAll() {
        return purchaseHeaderRepository.findAll().stream().map(this::toResponse).toList();
    }

    public PurchaseHeaderResponse getById(Integer id) {
        return purchaseHeaderRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found"));
    }


    @Transactional
    public void delete(Integer id) {
        PurchaseHeader header = purchaseHeaderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found"));


        List<InvoiceHeader> invoices = invoiceHeaderRepository
                .findByParentTypeAndParentId(InvoiceKind.PURCHASE, header.getId());

        for (InvoiceHeader invoice : invoices) {
            invoiceHeaderRepository.delete(invoice);
        }

        if (header.getStatus() == StatusType.receive) {
            //  Reverse stock before deletion
            for (PurchaseItem item : header.getItems()) {
                Integer productId = item.getProductId().getId();
                Integer unitItemId = item.getUnitItemId().getId();
                BigDecimal qty = item.getQty();

                stockService.decreaseStock(productId, header.getWarehouseId().getId(), unitItemId, qty);
            }
        }

        deleteJournalForTransfer(header.getId());

        purchaseHeaderRepository.delete(header);
    }


        public List<PurchaseHeaderResponse> getByStatus(StatusType status) {
            return purchaseHeaderRepository.findByStatus(status)
                    .stream()
                    .map(this::toResponse)
                    .toList();
        }





    private UnitItem resolveUnitItem(Product product, Integer unitItemId) {
        if (unitItemId != null) {
            return unitItemRepository.findById(unitItemId)
                    .orElseThrow(() -> new ResourceNotFoundException("UnitItem not found"));
        } else {
            return product.getDefaultUnit().getUnitItems().stream()
                    .filter(UnitItem::getIsDef)
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Default UnitItem not found"));
        }
    }

    @Transactional
    public void createTransferInvoices(PurchaseHeader purchaseHeader) {
        createSingleInvoiceFromTransfer(
                purchaseHeader.getWarehouseId().getId(),
                1, // buy invoice
                purchaseHeader
        );
    }



    @Transactional
    private void createSingleInvoiceFromTransfer(Integer warehouseId, Integer invoiceTypeId, PurchaseHeader purchaseHeader) {
        InvoiceHeader invoice = new InvoiceHeader();
        invoice.setWarehouse(warehouseRepository.getReferenceById(warehouseId));
        invoice.setInvoiceType(invoiceTypeRepository.getReferenceById(invoiceTypeId));
        invoice.setDate(purchaseHeader.getReceiveDate());
        invoice.setAccount(invoiceTypeRepository.getReferenceById(invoiceTypeId).getDefaultCashAccId());
        invoice.setCurrency(invoiceTypeRepository.getReferenceById(invoiceTypeId).getDefaultCurrencyId());
        invoice.setCurrencyValue(BigDecimal.valueOf(1));
        invoice.setIsPosted(true);
        invoice.setPostedDate(Boolean.TRUE.equals(invoice.getIsPosted()) && invoice.getPostedDate() == null ? LocalDateTime.now() : invoice.getPostedDate());
        invoice.setPayType(0);
        invoice.setParentType(InvoiceKind.PURCHASE);
        invoice.setParentId(purchaseHeader.getId());
        invoice.setIsSuspended(false);
        invoice.setInvoiceDiscounts(new ArrayList<>());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        invoice.setUser(currentUser);

        List<InvoiceItem> items = new ArrayList<>();

        for (PurchaseItem purchaseItem : purchaseHeader.getItems()) {

            Product product = purchaseItem.getProductId();

            UnitItem unitItem = resolveUnitItem(product, purchaseItem.getUnitItemId().getId());

            BigDecimal unitFact = purchaseItem.getUnitFact() != null
                    ? purchaseItem.getUnitFact()
                    : BigDecimal.valueOf(unitItem.getFact());

            InvoiceItem item = InvoiceItem.builder()
                    .invoiceHeader(invoice)
                    .product(product)
                    .qty(purchaseItem.getQty())
                    .price(BigDecimal.ZERO)
                    .unitItem(unitItem)
                    .unitFact(unitFact)
                    .bonusQty(BigDecimal.ZERO)
                    .build();

            items.add(item);
        }

        invoice.setInvoiceItems(items);
        invoiceHeaderRepository.save(invoice);
    }



    @Transactional
    public void createJournalForTransfer(PurchaseHeader purchaseHeader) {
        if (purchaseHeader.getTotal() == null || purchaseHeader.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            return; // No need for journal
        }

        JournalHeader header = JournalHeader.builder()
                .branch(purchaseHeader.getWarehouseId().getBranch())
                .date(purchaseHeader.getReceiveDate())
                .currency(currencyRepository.getReferenceById(1)) // Default currency
                .currencyValue(BigDecimal.ONE)
                .isPosted(true)
               // .postedDate(LocalDateTime.now())
                .kind(JournalKind.PURCHASE)
                .parentId(purchaseHeader.getId())
                .parentType(0)
                .build();

        Account cashAccount = accountRepository.getReferenceById(4);
        Account billAccount =  accountRepository.getReferenceById(5);

        JournalItem cash = JournalItem.builder()
                .journalHeader(header)
                .account(cashAccount)
                .debit(BigDecimal.ZERO)
                .credit(purchaseHeader.getTotal())
                .currency(header.getCurrency())
                .currencyValue(header.getCurrencyValue())
                .date(header.getDate())
                .build();

        JournalItem bill = JournalItem.builder()
                .journalHeader(header)
                .account(billAccount)
                .debit(purchaseHeader.getTotal())
                .credit(BigDecimal.ZERO)
                .currency(header.getCurrency())
                .currencyValue(header.getCurrencyValue())
                .date(header.getDate())
                .build();



        header.setJournalItems(List.of(cash, bill));


        BigDecimal totalDebit = header.getJournalItems().stream()
                .map(JournalItem::getDebit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCredit = header.getJournalItems().stream()
                .map(JournalItem::getCredit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new IllegalStateException("Journal entry is unbalanced! Debit: " + totalDebit + " ≠ Credit: " + totalCredit);
        }


        header.setDebit(totalDebit);
        header.setCredit(totalCredit);

        journalHeaderRepository.save(header);
    }

    @Transactional
    public void deleteJournalForTransfer(Integer purchaseId) {
        Optional<JournalHeader> journalOpt = journalHeaderRepository.findByKindAndParentId(JournalKind.PURCHASE, purchaseId);
        journalOpt.ifPresent(journalHeaderRepository::delete);
    }

    private PurchaseHeaderResponse toResponse(PurchaseHeader header) {
        return PurchaseHeaderResponse.builder()
                .id(header.getId())
                .warehouseId(header.getWarehouseId().getId())
                .warehouseName(header.getWarehouseId().getName())
                .supplyDate(header.getSupplyDate())
                .requestDate(header.getRequestDate())
                .buyDate(header.getBuyDate())
                .receiveDate(header.getReceiveDate())
                .notes(header.getNotes())
                .total(header.getTotal())
                .supplierName(header.getSupplierName())
                .status(String.valueOf(header.getStatus()))
                .items(header.getItems().stream().map(this::mapItem).toList())
                .build();
    }

    private PurchaseItemResponse mapItem(PurchaseItem item) {
        return PurchaseItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId().getId())
                .productName(item.getProductId().getName())
                .qty(item.getQty())
                .unitItemId(item.getUnitItemId() != null ? item.getUnitItemId().getId() : null)
                .unitFact(item.getUnitFact())
                .build();
    }
}
