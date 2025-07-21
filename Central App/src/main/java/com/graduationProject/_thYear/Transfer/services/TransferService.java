package com.graduationProject._thYear.Transfer.services;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Account.repositories.AccountRepository;
import com.graduationProject._thYear.Currency.repositories.CurrencyRepository;
import com.graduationProject._thYear.Invoice.models.InvoiceHeader;
import com.graduationProject._thYear.Invoice.models.InvoiceItem;
import com.graduationProject._thYear.Invoice.models.InvoiceKind;
import com.graduationProject._thYear.Invoice.repositories.InvoiceHeaderRepository;
import com.graduationProject._thYear.Invoice.services.InvoiceService;
import com.graduationProject._thYear.InvoiceType.repositories.InvoiceTypeRepository;
import com.graduationProject._thYear.Journal.models.JournalHeader;
import com.graduationProject._thYear.Journal.models.JournalItem;
import com.graduationProject._thYear.Journal.models.JournalKind;
import com.graduationProject._thYear.Journal.repositories.JournalHeaderRepository;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.repositories.ProductRepository;
import com.graduationProject._thYear.ProductStock.models.ProductStock;
import com.graduationProject._thYear.ProductStock.services.ProductStockService;
import com.graduationProject._thYear.Transfer.dtos.requests.CreateTransferRequest;
import com.graduationProject._thYear.Transfer.dtos.requests.CreateTransferItemRequest;
import com.graduationProject._thYear.Transfer.dtos.requests.UpdateTransferRequest;
import com.graduationProject._thYear.Transfer.dtos.responses.TransferItemResponse;
import com.graduationProject._thYear.Transfer.dtos.responses.TransferResponse;
import com.graduationProject._thYear.Transfer.models.Transfer;
import com.graduationProject._thYear.Transfer.models.TransferItem;
import com.graduationProject._thYear.Transfer.repositories.TransferItemRepository;
import com.graduationProject._thYear.Transfer.repositories.TransferRepository;
import com.graduationProject._thYear.Unit.models.UnitItem;
import com.graduationProject._thYear.Unit.repositories.UnitItemRepository;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import com.graduationProject._thYear.Warehouse.repositories.WarehouseRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final InvoiceHeaderRepository invoiceHeaderRepository;
    private final InvoiceTypeRepository invoiceTypeRepository;
    private final CurrencyRepository currencyRepository;
    private final WarehouseRepository warehouseRepo;
    private final AccountRepository accountRepo;
    private final ProductRepository productRepo;
    private final UnitItemRepository unitItemRepo;
    private final ProductStockService stockService;

    private final JournalHeaderRepository journalHeaderRepository;

    @Transactional
    public TransferResponse create(CreateTransferRequest request) {
        Warehouse fromWarehouse = warehouseRepo.findById(request.getFromWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("From warehouse not found"));
        Warehouse toWarehouse = warehouseRepo.findById(request.getToWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("To warehouse not found"));
        Account cashAccount = accountRepo.findById(request.getCashAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Cash account not found"));
        Account expenseAccount = accountRepo.findById(request.getExpenseAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Expense account not found"));

        Transfer transfer = Transfer.builder()
                .fromWarehouseId(fromWarehouse)
                .toWarehouseId(toWarehouse)
                .cashAccountId(cashAccount)
                .expenseAccountId(expenseAccount)
                .expenseValue(request.getExpenseValue())
                .date(request.getDate())
                .driverName(request.getDriverName())
                .notes(request.getNotes())
                .build();

        List<TransferItem> items = request.getItems().stream().map(itemReq -> {
            Product product = productRepo.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            UnitItem unitItem = resolveUnitItem(product, itemReq.getUnitItemId());

            BigDecimal unitFact = itemReq.getUnitFact() != null ? itemReq.getUnitFact() : BigDecimal.valueOf(unitItem.getFact());

            Optional<ProductStock> sourceStock = stockService.findStock(product.getId(), fromWarehouse.getId(), unitItem.getId());
            if (sourceStock.isEmpty()) {
                stockService.createStock(product.getId(), fromWarehouse.getId(), unitItem.getId(), BigDecimal.ZERO);
            }

            Optional<ProductStock> destStock = stockService.findStock(product.getId(), toWarehouse.getId(), unitItem.getId());
            if (destStock.isEmpty()) {
                stockService.createStock(product.getId(), toWarehouse.getId(), unitItem.getId(), BigDecimal.ZERO);
            }


            stockService.decreaseStock(product.getId(), fromWarehouse.getId(), unitItem.getId(), itemReq.getQty());
            stockService.increaseStock(product.getId(), toWarehouse.getId(), unitItem.getId(), itemReq.getQty());

            return TransferItem.builder()
                    .transfer(transfer)
                    .productId(product)
                    .qty(itemReq.getQty())
                    .unitItemId(unitItem)
                    .unitFact(unitFact)
                    .build();
        }).collect(Collectors.toList());

        transfer.setItems(items);
        Transfer saved = transferRepository.save(transfer);

        createTransferInvoices(transfer);

//        createJournalForTransfer(transfer);

        return toResponse(saved);
    }



    @Transactional
    public TransferResponse update(Integer id, UpdateTransferRequest req) {
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found"));

        if (req.getFromWarehouseId() != null) {
            Warehouse fromWarehouse = warehouseRepo.findById(req.getFromWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("From warehouse not found"));
            transfer.setFromWarehouseId(fromWarehouse);
        }

        if (req.getToWarehouseId() != null) {
            Warehouse toWarehouse = warehouseRepo.findById(req.getToWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("To warehouse not found"));
            transfer.setToWarehouseId(toWarehouse);
        }

        if (req.getCashAccountId() != null) {
            Account cash = accountRepo.findById(req.getCashAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cash account not found"));
            transfer.setCashAccountId(cash);
        }

        if (req.getExpenseAccountId() != null) {
            Account exp = accountRepo.findById(req.getExpenseAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Expense account not found"));
            transfer.setExpenseAccountId(exp);
        }

        if (req.getExpenseValue() != null)
            transfer.setExpenseValue(req.getExpenseValue());

        if (req.getDate() != null)
            transfer.setDate(req.getDate());

        if (req.getDriverName() != null)
            transfer.setDriverName(req.getDriverName());

        if (req.getNotes() != null)
            transfer.setNotes(req.getNotes());

        // If items are provided → fully replace with stock rollback and reapply
        if (req.getItems() != null && !req.getItems().isEmpty()) {
            //  Rollback old stock
            for (TransferItem item : transfer.getItems()) {
                stockService.increaseStock(item.getProductId().getId(), transfer.getFromWarehouseId().getId(), item.getUnitItemId().getId(), item.getQty());
                stockService.decreaseStock(item.getProductId().getId(), transfer.getToWarehouseId().getId(), item.getUnitItemId().getId(), item.getQty());
            }

            transfer.getItems().clear();

            //  Add new stock
            List<TransferItem> newItems = req.getItems().stream().map(itemReq -> {
                Product product = productRepo.findById(itemReq.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
                UnitItem unitItem = resolveUnitItem(product, itemReq.getUnitItemId());

                BigDecimal unitFact = itemReq.getUnitFact() != null ? itemReq.getUnitFact() : BigDecimal.valueOf(unitItem.getFact());

                stockService.decreaseStock(product.getId(), transfer.getFromWarehouseId().getId(), unitItem.getId(), itemReq.getQty());
                stockService.increaseStock(product.getId(), transfer.getToWarehouseId().getId(), unitItem.getId(), itemReq.getQty());

                return TransferItem.builder()
                        .transfer(transfer)
                        .productId(product)
                        .qty(itemReq.getQty())
                        .unitItemId(unitItem)
                        .unitFact(unitFact)
                        .build();
            }).toList();

            transfer.getItems().addAll(newItems);
        }

        Transfer transfer1 = transferRepository.save(transfer);
        recreateTransferInvoices(transfer1);
//        deleteJournalForTransfer(transfer.getId());
//        createJournalForTransfer(transfer);

        return toResponse(transfer1);
    }



    public List<TransferResponse> getAll() {
        return transferRepository.findAll().stream().map(this::toResponse).toList();
    }

    public TransferResponse getById(Integer id) {
        return transferRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found"));
    }

    public List<TransferResponse> filterByWarehouseAndDate(Integer warehouseId, LocalDateTime start, LocalDateTime end) {
        return transferRepository.findAll().stream()
                .filter(t -> (warehouseId == null || t.getFromWarehouseId().getId().equals(warehouseId)
                        || t.getToWarehouseId().getId().equals(warehouseId)) &&
                        (t.getDate().isAfter(start.minusSeconds(1)) && t.getDate().isBefore(end.plusSeconds(1)))
                )
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Integer id) {
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found"));


        List<InvoiceHeader> invoices = invoiceHeaderRepository
                .findByParentTypeAndParentId(InvoiceKind.TRANSFER, transfer.getId());

        for (InvoiceHeader invoice : invoices) {
            invoiceHeaderRepository.delete(invoice);
        }


        //  Reverse stock before deletion
        for (TransferItem item : transfer.getItems()) {
            Integer productId = item.getProductId().getId();
            Integer unitItemId = item.getUnitItemId().getId();
            BigDecimal qty = item.getQty();

            stockService.increaseStock(productId, transfer.getFromWarehouseId().getId(), unitItemId, qty);
            stockService.decreaseStock(productId, transfer.getToWarehouseId().getId(), unitItemId, qty);
        }

      //  deleteJournalForTransfer(transfer.getId());

        transferRepository.delete(transfer);
    }


    private UnitItem resolveUnitItem(Product product, Integer unitItemId) {
        if (unitItemId != null) {
            return unitItemRepo.findById(unitItemId)
                    .orElseThrow(() -> new ResourceNotFoundException("UnitItem not found"));
        } else {
            return product.getDefaultUnit().getUnitItems().stream()
                    .filter(UnitItem::getIsDef)
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Default UnitItem not found"));
        }
    }

    @Transactional
    public void recreateTransferInvoices(Transfer transfer) {
        List<InvoiceHeader> oldInvoices = invoiceHeaderRepository
                .findByParentTypeAndParentId(InvoiceKind.TRANSFER, transfer.getId());

        oldInvoices.forEach(invoiceHeaderRepository::delete);

        createTransferInvoices(transfer);
    }

    @Transactional
    public void createTransferInvoices(Transfer transfer) {
        createSingleInvoiceFromTransfer(
                transfer.getFromWarehouseId().getId(),
                2, // output invoice
                transfer
        );

        createSingleInvoiceFromTransfer(
                transfer.getToWarehouseId().getId(),
                1, // input invoice
                transfer
        );
    }


    private void createSingleInvoiceFromTransfer(Integer warehouseId, Integer invoiceTypeId, Transfer transfer) {
        InvoiceHeader invoice = new InvoiceHeader();
        invoice.setWarehouse(warehouseRepo.getReferenceById(warehouseId));
        invoice.setInvoiceType(invoiceTypeRepository.getReferenceById(invoiceTypeId));
        invoice.setDate(transfer.getDate());
        invoice.setAccount(accountRepo.getReferenceById(transfer.getCashAccountId().getId()));
        invoice.setCurrency(currencyRepository.getReferenceById(1));
        invoice.setCurrencyValue(BigDecimal.valueOf(1));
        invoice.setIsPosted(true);
        invoice.setPostedDate(Boolean.TRUE.equals(invoice.getIsPosted()) && invoice.getPostedDate() == null ? LocalDateTime.now() : invoice.getPostedDate());
        invoice.setPayType(0);
        invoice.setParentType(InvoiceKind.TRANSFER);
        invoice.setParentId(transfer.getId());
        invoice.setIsSuspended(false);
        invoice.setInvoiceDiscounts(new ArrayList<>());

        List<InvoiceItem> items = new ArrayList<>();

        for (TransferItem transferItem : transfer.getItems()) {

            Product product = transferItem.getProductId();

            UnitItem unitItem = resolveUnitItem(product, transferItem.getUnitItemId().getId());

            BigDecimal unitFact = transferItem.getUnitFact() != null
                    ? transferItem.getUnitFact()
                    : BigDecimal.valueOf(unitItem.getFact());

            InvoiceItem item = InvoiceItem.builder()
                    .invoiceHeader(invoice)
                    .product(product)
                    .qty(transferItem.getQty())
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



//    @Transactional
//    public void createJournalForTransfer(Transfer transfer) {
//        if (transfer.getExpenseValue() == null || transfer.getExpenseValue().compareTo(BigDecimal.ZERO) <= 0) {
//            return; // No need for journal
//        }
//
//        JournalHeader header = JournalHeader.builder()
//                .warehouse(transfer.getToWarehouseId())
//                .date(transfer.getDate())
//                .currency(currencyRepository.getReferenceById(1)) // Default currency
//                .currencyValue(BigDecimal.ONE)
//                .isPosted(true)
//               // .postedDate(LocalDateTime.now())
//                .kind(JournalKind.TRANSFER)
//                .parentId(transfer.getId())
//                .parentType(0)
//                .build();
//
//        Account cashAccount = transfer.getCashAccountId();
//        Account expenseAccount = transfer.getExpenseAccountId();
//
//        JournalItem cash = JournalItem.builder()
//                .journalHeader(header)
//                .account(cashAccount)
//                .debit(BigDecimal.ZERO)
//                .credit(transfer.getExpenseValue())
//                .currency(header.getCurrency())
//                .currencyValue(header.getCurrencyValue())
//                .date(header.getDate())
//                .build();
//
//        JournalItem expense = JournalItem.builder()
//                .journalHeader(header)
//                .account(expenseAccount)
//                .debit(transfer.getExpenseValue())
//                .credit(BigDecimal.ZERO)
//                .currency(header.getCurrency())
//                .currencyValue(header.getCurrencyValue())
//                .date(header.getDate())
//                .build();
//
//
//
//        header.setJournalItems(List.of(cash, expense));
//
//
//        BigDecimal totalDebit = header.getJournalItems().stream()
//                .map(JournalItem::getDebit)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        BigDecimal totalCredit = header.getJournalItems().stream()
//                .map(JournalItem::getCredit)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//
//        if (totalDebit.compareTo(totalCredit) != 0) {
//            throw new IllegalStateException("Journal entry is unbalanced! Debit: " + totalDebit + " ≠ Credit: " + totalCredit);
//        }
//
//
//        header.setDebit(totalDebit);
//        header.setCredit(totalCredit);
//
//        journalHeaderRepository.save(header);
//    }
//
//    @Transactional
//    public void deleteJournalForTransfer(Integer transferId) {
//        Optional<JournalHeader> journalOpt = journalHeaderRepository.findByKindAndParentId(JournalKind.TRANSFER, transferId);
//        journalOpt.ifPresent(journalHeaderRepository::delete);
//    }

    private TransferResponse toResponse(Transfer t) {
        return TransferResponse.builder()
                .id(t.getId())
                .fromWarehouseId(t.getFromWarehouseId().getId())
                .fromWarehouseName(t.getFromWarehouseId().getName())
                .toWarehouseId(t.getToWarehouseId().getId())
                .toWarehouseName(t.getToWarehouseId().getName())
                .cashAccountId(t.getCashAccountId().getId())
                .expenseAccountId(t.getExpenseAccountId().getId())
                .expenseValue(t.getExpenseValue())
                .date(t.getDate())
                .driverName(t.getDriverName())
                .notes(t.getNotes())
                .items(t.getItems().stream().map(this::mapItem).toList())
                .build();
    }

    private TransferItemResponse mapItem(TransferItem i) {
        return TransferItemResponse.builder()
                .id(i.getId())
                .productId(i.getProductId().getId())
                .productName(i.getProductId().getName())
                .qty(i.getQty())
                .unitItemId(i.getUnitItemId() != null ? i.getUnitItemId().getId() : null)
                .unitFact(i.getUnitFact())
                .build();
    }
}
