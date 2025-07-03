package com.graduationProject._thYear.InvoiceType.services;

import com.graduationProject._thYear.Currency.models.Currency;
import com.graduationProject._thYear.Currency.repositories.CurrencyRepository;
import com.graduationProject._thYear.InvoiceType.dtos.requests.CreateInvoiceTypeRequest;
import com.graduationProject._thYear.InvoiceType.dtos.requests.UpdateInvoiceTypeRequest;
import com.graduationProject._thYear.InvoiceType.dtos.responses.InvoiceTypeResponse;
import com.graduationProject._thYear.InvoiceType.models.InvoiceType;
import com.graduationProject._thYear.InvoiceType.repositories.InvoiceTypeRepository;
import com.graduationProject._thYear.Product.models.Price;
import com.graduationProject._thYear.Product.repositories.PriceRepository;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import com.graduationProject._thYear.Warehouse.repositories.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceTypeServiceImpl implements InvoiceTypeService{

    private final InvoiceTypeRepository invoiceTypeRepository;
    private final PriceRepository priceRepository;
    private final WarehouseRepository warehouseRepository;
    private final CurrencyRepository currencyRepository;


    @Override
    @Transactional
    public InvoiceTypeResponse create(CreateInvoiceTypeRequest dto) {
        InvoiceType invoiceType = mapToEntity(dto);
        InvoiceType saved = invoiceTypeRepository.save(invoiceType);
        return mapToResponse(saved);
    }

    @Override
    public InvoiceTypeResponse getById(Integer id) {
        InvoiceType invoiceType = invoiceTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("InvoiceType not found with ID: " + id));
        return mapToResponse(invoiceType);
    }

    @Override
    public List<InvoiceTypeResponse> getAll() {
        return invoiceTypeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InvoiceTypeResponse update(Integer id, UpdateInvoiceTypeRequest dto) {
        InvoiceType existing = invoiceTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("InvoiceType not found with ID: " + id));

        updateEntity(existing, dto);
        InvoiceType updated = invoiceTypeRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!invoiceTypeRepository.existsById(id)) {
            throw new RuntimeException("InvoiceType not found with ID: " + id);
        }
        invoiceTypeRepository.deleteById(id);
    }

    // 🔄 Mapping Helpers

    private InvoiceType mapToEntity(CreateInvoiceTypeRequest dto) {
        return InvoiceType.builder()
                .type(dto.getType())
                .name(dto.getName())
                .defaultPriceId(findPrice(dto.getDefaultPriceId()))
                .minDefaultPriceId(findPrice(dto.getMinDefaultPriceId()))
                .isAffectCostPrice(dto.getIsAffectCostPrice())
                .isAffectLastPrice(dto.getIsAffectLastPrice())
                .isAffectCustPrice(dto.getIsAffectCustPrice())
                .isAffectProfit(dto.getIsAffectProfit())
                .isDiscAffectCost(dto.getIsDiscAffectCost())
                .isExtraAffectCost(dto.getIsExtraAffectCost())
                .isNoEntry(dto.getIsNoEntry())
                .isAutoEntry(dto.getIsAutoEntry())
                .isAutoEntryPost(dto.getIsAutoEntryPost())
                .isNoPost(dto.getIsNoPost())
                .isAutoPost(dto.getIsAutoPost())
                .defaultWarehouseId(findWarehouse(dto.getDefaultWarehouseId()))
                .defaultBillAccId(dto.getDefaultBillAccId())
                .defaultCashAccId(dto.getDefaultCashAccId())
                .defaultDiscAccId(dto.getDefaultDiscAccId())
                .defaultExtraAccId(dto.getDefaultExtraAccId())
                .defaultCostAccId(dto.getDefaultCostAccId())
                .defaultStockAccId(dto.getDefaultStockAccId())
                .isShortEntry(dto.getIsShortEntry())
                .isCashBill(dto.getIsCashBill())
                .printAfterInsert(dto.getPrintAfterInsert())
                .isBarcode(dto.getIsBarcode())
                .defaultCurrencyId(findCurrency(dto.getDefaultCurrencyId()))
                .build();
    }

    private void updateEntity(InvoiceType entity, UpdateInvoiceTypeRequest dto) {
        entity.setType(dto.getType());
        entity.setName(dto.getName());
        entity.setDefaultPriceId(findPrice(dto.getDefaultPriceId()));
        entity.setMinDefaultPriceId(findPrice(dto.getMinDefaultPriceId()));
        entity.setIsAffectCostPrice(dto.getIsAffectCostPrice());
        entity.setIsAffectLastPrice(dto.getIsAffectLastPrice());
        entity.setIsAffectCustPrice(dto.getIsAffectCustPrice());
        entity.setIsAffectProfit(dto.getIsAffectProfit());
        entity.setIsDiscAffectCost(dto.getIsDiscAffectCost());
        entity.setIsExtraAffectCost(dto.getIsExtraAffectCost());
        entity.setIsNoEntry(dto.getIsNoEntry());
        entity.setIsAutoEntry(dto.getIsAutoEntry());
        entity.setIsAutoEntryPost(dto.getIsAutoEntryPost());
        entity.setIsNoPost(dto.getIsNoPost());
        entity.setIsAutoPost(dto.getIsAutoPost());
        entity.setDefaultWarehouseId(findWarehouse(dto.getDefaultWarehouseId()));
        entity.setDefaultBillAccId(dto.getDefaultBillAccId());
        entity.setDefaultCashAccId(dto.getDefaultCashAccId());
        entity.setDefaultDiscAccId(dto.getDefaultDiscAccId());
        entity.setDefaultExtraAccId(dto.getDefaultExtraAccId());
        entity.setDefaultCostAccId(dto.getDefaultCostAccId());
        entity.setDefaultStockAccId(dto.getDefaultStockAccId());
        entity.setIsShortEntry(dto.getIsShortEntry());
        entity.setIsCashBill(dto.getIsCashBill());
        entity.setPrintAfterInsert(dto.getPrintAfterInsert());
        entity.setIsBarcode(dto.getIsBarcode());
        entity.setDefaultCurrencyId(findCurrency(dto.getDefaultCurrencyId()));
    }

    private InvoiceTypeResponse mapToResponse(InvoiceType entity) {
        return InvoiceTypeResponse.builder()
                .id(entity.getId())
                .type(entity.getType())
                .name(entity.getName())
                .defaultPriceId(entity.getDefaultPriceId() != null ? entity.getDefaultPriceId().getId() : null)
                .minDefaultPriceId(entity.getMinDefaultPriceId() != null ? entity.getMinDefaultPriceId().getId() : null)
                .isAffectCostPrice(entity.getIsAffectCostPrice())
                .isAffectLastPrice(entity.getIsAffectLastPrice())
                .isAffectCustPrice(entity.getIsAffectCustPrice())
                .isAffectProfit(entity.getIsAffectProfit())
                .isDiscAffectCost(entity.getIsDiscAffectCost())
                .isExtraAffectCost(entity.getIsExtraAffectCost())
                .isNoEntry(entity.getIsNoEntry())
                .isAutoEntry(entity.getIsAutoEntry())
                .isAutoEntryPost(entity.getIsAutoEntryPost())
                .isNoPost(entity.getIsNoPost())
                .isAutoPost(entity.getIsAutoPost())
                .defaultWarehouseId(entity.getDefaultWarehouseId() != null ? entity.getDefaultWarehouseId().getId() : null)
                .defaultBillAccId(entity.getDefaultBillAccId())
                .defaultCashAccId(entity.getDefaultCashAccId())
                .defaultDiscAccId(entity.getDefaultDiscAccId())
                .defaultExtraAccId(entity.getDefaultExtraAccId())
                .defaultCostAccId(entity.getDefaultCostAccId())
                .defaultStockAccId(entity.getDefaultStockAccId())
                .isShortEntry(entity.getIsShortEntry())
                .isCashBill(entity.getIsCashBill())
                .printAfterInsert(entity.getPrintAfterInsert())
                .isBarcode(entity.getIsBarcode())
                .defaultCurrencyId(entity.getDefaultCurrencyId() != null ? entity.getDefaultCurrencyId().getId() : null)
                .build();
    }

    private Price findPrice(Integer id) {
        return priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Price not found: " + id));
    }

    private Warehouse findWarehouse(Integer id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found: " + id));
    }

    private Currency findCurrency(Integer id) {
        return currencyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Currency not found: " + id));
    }

}
