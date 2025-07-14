package com.graduationProject._thYear.Invoice.services;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Currency.models.Currency;
import com.graduationProject._thYear.Invoice.dtos.requests.*;
import com.graduationProject._thYear.Invoice.dtos.responses.*;
import com.graduationProject._thYear.Invoice.dtos.responses.DailyMovementResponse.DailyMovemntMainItems;
import com.graduationProject._thYear.Invoice.dtos.responses.DailyMovementResponse.DailyMovemntSideItems;
import com.graduationProject._thYear.Invoice.dtos.responses.ProductStockResponse.ProductStockMainItems;
import com.graduationProject._thYear.Invoice.dtos.responses.ProductStockResponse.ProductStockSideItems;
import com.graduationProject._thYear.Invoice.models.*;
import com.graduationProject._thYear.Invoice.repositories.*;
import com.graduationProject._thYear.InvoiceType.models.InvoiceType;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.repositories.ProductRepository;
import com.graduationProject._thYear.ProductStock.models.ProductStock;
import com.graduationProject._thYear.ProductStock.services.ProductStockService;
import com.graduationProject._thYear.Unit.models.UnitItem;
import com.graduationProject._thYear.Unit.repositories.UnitItemRepository;
import com.graduationProject._thYear.Account.repositories.AccountRepository;
import com.graduationProject._thYear.InvoiceType.repositories.InvoiceTypeRepository;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import com.graduationProject._thYear.Warehouse.repositories.WarehouseRepository;
import com.graduationProject._thYear.Currency.repositories.CurrencyRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceHeaderRepository headerRepo;
    private final ProductRepository productRepo;
    private final UnitItemRepository unitItemRepo;
    private final WarehouseRepository warehouseRepo;
    private final AccountRepository accountRepo;
    // private final InvoiceItemRepository ItemRepo;
    private final InvoiceTypeRepository invoiceTypeRepo;
    private final CurrencyRepository currencyRepo;
    private final AccountRepository accountRepository;
    private final ProductStockService stockService;

    @Transactional
    public InvoiceResponse create(CreateInvoiceRequest req) {
        Warehouse warehouse = warehouseRepo.findById(req.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("warehouse  not found"));
        Account account = accountRepo.findById(req.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("account  not found"));
        InvoiceType type = invoiceTypeRepo.findById(req.getInvoiceTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("invoiceType not found"));
        InvoiceType invoiceType = invoiceTypeRepo.findById(req.getInvoiceTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("invoiceType  not found"));
        Currency currency = currencyRepo.findById(req.getCurrencyId())
                .orElseThrow(() -> new ResourceNotFoundException("currency  not found"));

        InvoiceHeader invoice = InvoiceHeader.builder()
                .warehouse(warehouse)
                .account(account)
                .invoiceType(invoiceType)
                .currency(currency)
                .currencyValue(req.getCurrencyValue())
                .date(Optional.ofNullable(req.getDate()).orElse(LocalDateTime.now()))
                .isSuspended(Optional.ofNullable(req.getIsSuspended()).orElse(false))
                .isPosted(Boolean.TRUE.equals(req.getIsPosted()))
                .payType(req.getPayType())
                .notes(req.getNotes())
                .postedDate(Boolean.TRUE.equals(req.getIsPosted()) && req.getPostedDate() == null ? LocalDateTime.now() : req.getPostedDate())
                .build();


        List<InvoiceItem> items = req.getInvoiceItems().stream().map(itemReq -> {
            Product product = productRepo.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException(" product  not found"));
            UnitItem unitItem = resolveUnitItem(product, itemReq.getUnitItemId());
            BigDecimal unitFact = Optional.ofNullable(itemReq.getUnitFact()).orElse(BigDecimal.valueOf(unitItem.getFact()));

            return InvoiceItem.builder()
                    .invoiceHeader(invoice)
                    .product(product)
                    .qty(itemReq.getQty())
                    .price(itemReq.getPrice())
                    .bonusQty(Optional.ofNullable(itemReq.getBonusQty()).orElse(BigDecimal.ZERO))
                    .unitItem(unitItem)
                    .unitFact(unitFact)
                    .notes(itemReq.getNotes())
                    .build();
        }).collect(Collectors.toList());

        invoice.setInvoiceItems(items);




        List<InvoiceDiscount> discounts = Optional.ofNullable(req.getInvoiceDiscounts()).orElse(Collections.emptyList()).stream()
                .map(discountReq ->{
                    Account account1 = accountRepository.findById(discountReq.getAccount())
                            .orElseThrow(() -> new RuntimeException("account not found"));
                    return  InvoiceDiscount.builder()
                        .invoiceHeader(invoice)
                        .account(account1)
                        .discount(discountReq.getDiscount())
                        .extra(discountReq.getExtra())
                        .notes(discountReq.getNotes())
                        .build();
                })
                .collect(Collectors.toList());

        invoice.setInvoiceDiscounts(discounts);

        invoice.setTotal(calculateTotal(items));
        invoice.setTotalDisc(calculateTotalDiscounts(discounts));
        invoice.setTotalExtra(calculateTotalExtras(discounts));

        InvoiceHeader saved = headerRepo.save(invoice);

        if (saved.getIsPosted()) {
            adjustStock(items, warehouse.getId(), type, false);
        }

        return toResponse(saved);
    }

    public InvoiceResponse getById(Integer id) {
        return headerRepo.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }

    public List<InvoiceResponse> getAll() {
        return headerRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public InvoiceResponse update(Integer id, UpdateInvoiceRequest req) {
        InvoiceHeader invoice = headerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        boolean wasPosted = invoice.getIsPosted();

        if (Boolean.TRUE.equals(wasPosted)) {
            throw new RuntimeException("Invoice is posted. Cannot update.");
        }

        List<InvoiceItem> oldItems = new ArrayList<>(invoice.getInvoiceItems());
        InvoiceType oldType = invoice.getInvoiceType();

        if (req.getWarehouseId() != null)
            invoice.setWarehouse(warehouseRepo.findById(req.getWarehouseId())
                    .orElseThrow(() -> new RuntimeException("warehouse  not found")));

        if (req.getAccountId() != null)
            invoice.setAccount(accountRepo.findById(req.getAccountId())
                    .orElseThrow(() -> new RuntimeException("account  not found")));

        if (req.getInvoiceTypeId() != null)
            invoice.setInvoiceType(invoiceTypeRepo.findById(req.getInvoiceTypeId())
                    .orElseThrow(() -> new RuntimeException("InvoiceType  not found")));

        if (req.getCurrencyId() != null)
            invoice.setCurrency(currencyRepo.findById(req.getCurrencyId())
                    .orElseThrow(() -> new RuntimeException("Currency  not found")));

        if (req.getCurrencyValue() != null)
            invoice.setCurrencyValue(req.getCurrencyValue());

        if (req.getDate() != null) invoice.setDate(req.getDate());
        if (req.getNotes() != null) invoice.setNotes(req.getNotes());
        if (req.getIsSuspended() != null) invoice.setIsSuspended(req.getIsSuspended());
        if (req.getPayType() != null) invoice.setPayType(req.getPayType());
        if (req.getIsPosted() != null) {
            invoice.setIsPosted(req.getIsPosted());

            if (req.getIsPosted() && req.getPostedDate() == null) {
                invoice.setPostedDate(LocalDateTime.now());
            }
        }

        if (req.getPostedDate() != null) {
            invoice.setPostedDate(req.getPostedDate());
        }

        // Update items if provided
        if (req.getInvoiceItems() != null) {
            invoice.getInvoiceItems().clear();
            invoice.getInvoiceItems().addAll(req.getInvoiceItems().stream().map(itemReq -> {
                Product product = productRepo.findById(itemReq.getProductId())
                        .orElseThrow(() -> new RuntimeException("product  not found"));
                UnitItem unitItem = resolveUnitItem(product, itemReq.getUnitItemId());
                BigDecimal unitFact = Optional.ofNullable(itemReq.getUnitFact()).orElse(BigDecimal.valueOf(unitItem.getFact()));
                return InvoiceItem.builder()
                        .invoiceHeader(invoice)
                        .product(product)
                        .qty(itemReq.getQty())
                        .price(itemReq.getPrice())
                        .bonusQty(Optional.ofNullable(itemReq.getBonusQty()).orElse(BigDecimal.ZERO))
                        .unitItem(unitItem)
                        .unitFact(unitFact)
                        .notes(itemReq.getNotes())
                        .build();
            }).toList());
        }

        if (req.getInvoiceDiscounts() != null) {
            invoice.getInvoiceDiscounts().clear();
            invoice.getInvoiceDiscounts().addAll(req.getInvoiceDiscounts().stream().map(d ->
            { Account account1 = accountRepository.findById(d.getAccount())
                    .orElseThrow(() -> new RuntimeException("account not found"));
                return
                    InvoiceDiscount.builder()
                    .invoiceHeader(invoice)
                    .account(account1)
                    .discount(d.getDiscount())
                    .extra(d.getExtra())
                    .notes(d.getNotes())
                    .build();
            }).toList());
        }

        // Recalculate
        invoice.setTotal(calculateTotal(invoice.getInvoiceItems()));
        invoice.setTotalDisc(calculateTotalDiscounts(invoice.getInvoiceDiscounts()));
        invoice.setTotalExtra(calculateTotalExtras(invoice.getInvoiceDiscounts()));

        InvoiceHeader updated = headerRepo.save(invoice);
        if (wasPosted) {
            adjustStock(oldItems, invoice.getWarehouse().getId(), oldType, true);
        }

        if (updated.getIsPosted()) adjustStock(updated.getInvoiceItems(), updated.getWarehouse().getId(), updated.getInvoiceType(), false);


        return toResponse(updated);
    }

    public void delete(Integer id) {
        InvoiceHeader invoice = headerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("invoice not found with id: " + id));
        if (invoice.getIsPosted()) {
            adjustStock(invoice.getInvoiceItems(), invoice.getWarehouse().getId(), invoice.getInvoiceType(), true);
        }

        headerRepo.delete(invoice);
    }
//
//    public List<MaterialMovementResponse> reportMaterialMovement(LocalDate startDate, LocalDate endDate,Integer productId, Integer groupId, Integer warehouseId){
//        List<MaterialMovementResponse> result = new LinkedList<>();
//        List<Tuple> headers = headerRepo.getMaterialMovementHeader(
//            startDate.atStartOfDay(),
//            endDate.plusDays(1).atStartOfDay(),
//            productId ,
//            groupId,
//            warehouseId
//        );
//        for (Tuple header: headers){
//            MaterialMovementResponse responseHeader = MaterialMovementResponse.fromTuple(header);
//            List<Tuple> items = headerRepo.getMaterialMovementItems(
//                startDate.atStartOfDay(),
//                endDate.plusDays(1).atStartOfDay(),
//                (Integer) header.get("product_id"),
//                warehouseId
//            );
//            for (Tuple item : items){
//                responseHeader.addItem(MaterialMovementResponse.MaterialMovementItem.fromTuple(item));
//            }
//            result.add(responseHeader);
//        }
//        return result;
//    }
//
//    public DailyMovementResponse reportDailyMovement(LocalDate startDate, LocalDate endDate,Integer productId, Integer groupId, Integer warehouseId){
//        LocalDateTime startDateTime = startDate.atStartOfDay();
//        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();
//
//        List<Tuple> mainItemsTuples = headerRepo.getDailyMovementMainItems(startDateTime, endDateTime, productId, groupId, warehouseId);
//        List<Tuple> sideItemsTuples = headerRepo.getDailyMovementSideItems(startDateTime, endDateTime, productId, groupId, warehouseId);
//
//        var response = DailyMovementResponse.builder()
//            .startDate(startDate)
//            .endDate(endDate)
//            .currency("ل.س")
//            .mainItems(mainItemsTuples.stream()
//                .map((tuple) -> DailyMovemntMainItems.fromTuple(tuple))
//                .collect(Collectors.toList()))
//            .sideItems(sideItemsTuples.stream()
//                .map((tuple) -> DailyMovemntSideItems.fromTuple(tuple))
//                .collect(Collectors.toList()))
//            .build();
//        return response;
//    }
//
//    public ProductStockResponse reportProductStock(LocalDate startDate, LocalDate endDate,Integer productId, Integer groupId, Integer warehouseId){
//        LocalDateTime startDateTime = startDate.atStartOfDay();
//        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();
//
//        List<Tuple> mainItemsTuples = headerRepo.getProductStockMainItems(startDateTime, endDateTime, productId, groupId, warehouseId);
//        Tuple sideItemsTuples = headerRepo.getProductStockSideItems(startDateTime, endDateTime, productId, groupId, warehouseId);
//
//        var response = ProductStockResponse.builder()
//            .startDate(startDate)
//            .endDate(endDate)
//            .currency("ل.س")
//            .mainItems(mainItemsTuples.stream()
//                .map((tuple) -> ProductStockMainItems.fromTuple(tuple))
//                .collect(Collectors.toList()))
//            .sideItems(ProductStockSideItems.fromTuple(sideItemsTuples))
//            .build();
//        return response;
//    }


    private void adjustStock(List<InvoiceItem> items, Integer warehouseId, InvoiceType type, boolean reverse) {
        for (InvoiceItem item : items) {
            int productId = item.getProduct().getId();
            int unitItemId = item.getUnitItem().getId();
            BigDecimal qty = item.getQty();

            boolean shouldDecrease = type.isStockOut() != reverse;
            boolean shouldIncrease = type.isStockIn() != reverse;

            Optional<ProductStock> optionalStock = stockService.findStock(productId, warehouseId, unitItemId);

            if (shouldDecrease) {
                ProductStock stock = optionalStock.orElseThrow(() ->
                        new RuntimeException("Cannot decrease quantity, no stock found for this product: " + item.getProduct().getName()));
                stockService.decreaseStock(productId, warehouseId, unitItemId, qty);

            } else if (shouldIncrease) {
                if (optionalStock.isPresent()) {
                    stockService.increaseStock(productId, warehouseId, unitItemId, qty);
                } else {
                    stockService.createStock(productId, warehouseId, unitItemId, qty);
                }
            }
        }
    }

    private BigDecimal calculateTotal(List<InvoiceItem> items) {
        return items.stream()
                .map(i -> i.getPrice().multiply(i.getQty()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalDiscounts(List<InvoiceDiscount> discounts) {
        return discounts.stream()
                .map(d -> d.getDiscount() != null ? d.getDiscount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalExtras(List<InvoiceDiscount> discounts) {
        return discounts.stream()
                .map(d -> d.getExtra() != null ? d.getExtra() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private UnitItem resolveUnitItem(Product product, Integer unitItemId) {
        if (unitItemId != null) {
            return unitItemRepo.findById(unitItemId).orElseThrow(() -> new RuntimeException("unitItemId  not found"));
        } else {
            return product.getDefaultUnit().getUnitItems().stream()
                    .filter(UnitItem::getIsDef)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Default unit item not found"));
        }
    }

    private InvoiceResponse toResponse(InvoiceHeader h) {
        return InvoiceResponse.builder()
                .id(h.getId())
                .warehouseId(h.getWarehouse().getId())
                .warehouseName(h.getWarehouse().getName())
                .invoiceTypeId(h.getInvoiceType().getId())
                .invoiceTypeName(h.getInvoiceType().getName())
                .accountId(h.getAccount().getId())
                .currencyId(h.getCurrency().getId())
                .currencyValue(h.getCurrencyValue())
                .date(h.getDate())
                .isSuspended(h.getIsSuspended())
                .isPosted(h.getIsPosted())
                .payType(h.getPayType())
                .notes(h.getNotes())
                .postedDate(h.getPostedDate())
                .total(h.getTotal())
                .totalDisc(h.getTotalDisc())
                .totalExtra(h.getTotalExtra())
                .invoiceItems(h.getInvoiceItems().stream().map(this::mapItem).toList())
                .invoiceDiscounts(h.getInvoiceDiscounts().stream().map(this::mapDiscount).toList())
                .build();
    }

    private InvoiceItemResponse mapItem(InvoiceItem i) {
        return InvoiceItemResponse.builder()
                .id(i.getId())
                .invoiceHeaderId(i.getInvoiceHeader().getId())
                .productId(i.getProduct().getId())
                .productName(i.getProduct().getName())
                .qty(i.getQty())
                .price(i.getPrice())
                .bonusQty(i.getBonusQty())
                .unitItemId(i.getUnitItem().getId())
                .unitItemName(i.getUnitItem().getName())
                .unitFact(i.getUnitFact())
                .notes(i.getNotes())
                .build();
    }

    private InvoiceDiscountResponse mapDiscount(InvoiceDiscount d) {
        return InvoiceDiscountResponse.builder()
                .id(d.getId())
                .invoiceHeaderId(d.getInvoiceHeader().getId())
                .account(d.getAccount().getId())
                .discount(d.getDiscount())
                .extra(d.getExtra())
                .notes(d.getNotes())
                .build();
    }


}
