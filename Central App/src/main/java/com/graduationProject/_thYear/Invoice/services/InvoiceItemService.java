package com.graduationProject._thYear.Invoice.services;

import com.graduationProject._thYear.EventSyncronization.Records.InvoiceRecord.InvoiceItemRecord;
import com.graduationProject._thYear.Invoice.dtos.requests.CreateInvoiceItemRequest;
import com.graduationProject._thYear.Invoice.dtos.responses.InvoiceItemResponse;
import com.graduationProject._thYear.Invoice.dtos.requests.UpdateInvoiceItemRequest;
import com.graduationProject._thYear.Invoice.models.InvoiceHeader;
import com.graduationProject._thYear.Invoice.models.InvoiceItem;
import com.graduationProject._thYear.Invoice.repositories.InvoiceItemRepository;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.repositories.ProductRepository;
import com.graduationProject._thYear.Unit.models.UnitItem;
import com.graduationProject._thYear.Unit.repositories.UnitItemRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import com.graduationProject._thYear.Invoice.repositories.InvoiceHeaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceItemService {

    private final InvoiceItemRepository invoiceItemRepository;
    private final ProductRepository productRepository;
    private final UnitItemRepository unitItemRepository;
    private final InvoiceHeaderRepository invoiceHeaderRepository;

    @Transactional
    public InvoiceItemResponse create(CreateInvoiceItemRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        InvoiceHeader invoiceHeader = invoiceHeaderRepository.findById(request.getInvoiceHeaderId())
                .orElseThrow(() -> new RuntimeException("Invoice header not found"));

        UnitItem unitItem = null;
        BigDecimal unitFact = request.getUnitFact();

        if (request.getUnitItemId() != null) {
            unitItem = unitItemRepository.findById(request.getUnitItemId())
                    .orElseThrow(() -> new RuntimeException("UnitItem not found"));
            if (unitFact == null) unitFact = BigDecimal.valueOf(unitItem.getFact());
        } else {
            unitItem = product.getDefaultUnit().getUnitItems()
                    .stream()
                    .filter(UnitItem::getIsDef)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Default UnitItem not found"));
            unitFact = BigDecimal.valueOf(unitItem.getFact());
        }

        InvoiceItem item = InvoiceItem.builder()
                .invoiceHeader(invoiceHeader)
                .product(product)
                .qty(request.getQty())
                .price(request.getPrice())
                .bonusQty(Optional.ofNullable(request.getBonusQty()).orElse(BigDecimal.ZERO))
                .unitItem(unitItem)
                .unitFact(unitFact)
                .notes(request.getNotes())
                .build();

        invoiceItemRepository.save(item);
        return toResponse(item);
    }

    public List<InvoiceItemResponse> getAll() {
        return invoiceItemRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public InvoiceItemResponse getById(Integer id) {
        return invoiceItemRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("InvoiceItem not found"));
    }

    @Transactional
    public InvoiceItemResponse update(Integer id, UpdateInvoiceItemRequest request) {
        InvoiceItem item = invoiceItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("InvoiceItem not found"));

        if (request.getProductId() != null) {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            item.setProduct(product);

            // Also update default unit item if needed
            if (request.getUnitItemId() == null) {
                UnitItem defaultUnitItem = product.getDefaultUnit().getUnitItems().stream()
                        .filter(UnitItem::getIsDef)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Default UnitItem not found"));
                item.setUnitItem(defaultUnitItem);
                item.setUnitFact(BigDecimal.valueOf(defaultUnitItem.getFact()));
            }
        }

        if (request.getQty() != null) item.setQty(request.getQty());
        if (request.getPrice() != null) item.setPrice(request.getPrice());
        if (request.getBonusQty() != null) item.setBonusQty(request.getBonusQty());

        if (request.getUnitItemId() != null) {
            UnitItem unitItem = unitItemRepository.findById(request.getUnitItemId())
                    .orElseThrow(() -> new RuntimeException("UnitItem not found"));
            item.setUnitItem(unitItem);

            if (request.getUnitFact() == null) {
                item.setUnitFact(BigDecimal.valueOf(unitItem.getFact()));
            }
        }

        if (request.getUnitFact() != null) item.setUnitFact(request.getUnitFact());
        if (request.getNotes() != null) item.setNotes(request.getNotes());

        return toResponse(invoiceItemRepository.save(item));
    }

    public void delete(Integer id) {
        invoiceItemRepository.deleteById(id);
    }

    public List<InvoiceItem> saveOrUpdateBulk(List<InvoiceItemRecord> records, InvoiceHeader invoiceHeader){
        return records.stream()
            .map(record -> saveOrUpdate(record, invoiceHeader))
            .collect(Collectors.toList());
    }
    
    public InvoiceItem saveOrUpdate(InvoiceItemRecord invoiceItemRecord, InvoiceHeader invoiceHeader){
        Product product = productRepository.findByGlobalId(invoiceItemRecord.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("product id not found for invoice item sync"));
        UnitItem unitItem = unitItemRepository.findByGlobalId(invoiceItemRecord.getUnitItemId())
            .orElseThrow(() -> new ResourceNotFoundException("unit item id not found for invoice item sync"));

        return InvoiceItem.builder()
            .product(product)
            .unitItem(unitItem)
            .invoiceHeader(invoiceHeader)
            .unitFact(invoiceItemRecord.getUnitFact())
            .bonusQty(invoiceItemRecord.getBonusQty())
            .qty(invoiceItemRecord.getQty())
            .price(invoiceItemRecord.getPrice())
            .notes(invoiceItemRecord.getNotes())
            .build();
    }

    private InvoiceItemResponse toResponse(InvoiceItem item) {
        return InvoiceItemResponse.builder()
                .id(item.getId())
                .invoiceHeaderId(item.getInvoiceHeader() != null ?item.getInvoiceHeader().getId() : null)
                .productId(item.getProduct() != null ?item.getProduct().getId() : null)
                .productName(item.getProduct() != null ?item.getProduct().getName() : null)
                .qty(item.getQty())
                .price(item.getPrice())
                .bonusQty(item.getBonusQty())
                .unitItemId(item.getUnitItem() != null ? item.getUnitItem().getId() : null)
                .unitItemName(item.getUnitItem() != null ? item.getUnitItem().getName() : null)
                .unitFact(item.getUnitFact())
                .notes(item.getNotes())
                .build();
    }


}
