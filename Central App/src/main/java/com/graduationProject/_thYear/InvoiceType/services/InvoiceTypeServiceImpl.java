package com.graduationProject._thYear.InvoiceType.services;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Account.repositories.AccountRepository;
import com.graduationProject._thYear.Currency.models.Currency;
import com.graduationProject._thYear.Currency.repositories.CurrencyRepository;
import com.graduationProject._thYear.InvoiceType.dtos.requests.CreateInvoiceTypeRequest;
import com.graduationProject._thYear.InvoiceType.dtos.requests.UpdateInvoiceTypeRequest;
import com.graduationProject._thYear.InvoiceType.dtos.responses.InvoiceTypeResponse;
import com.graduationProject._thYear.InvoiceType.models.InvoiceType;
import com.graduationProject._thYear.InvoiceType.models.Type;
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
    private final AccountRepository accountRepository;


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


    private InvoiceType mapToEntity(CreateInvoiceTypeRequest dto) {
        return InvoiceType.builder()
                .type(Type.fromCode(dto.getType()))
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
                .defaultBillAccId(findAccount(dto.getDefaultBillAccId()))
                .defaultCashAccId(findAccount(dto.getDefaultCashAccId()))
                .defaultDiscAccId(findAccount(dto.getDefaultDiscAccId()))
                .defaultExtraAccId(findAccount(dto.getDefaultExtraAccId()))
                .defaultCostAccId(findAccount(dto.getDefaultCostAccId()))
                .defaultStockAccId(findAccount(dto.getDefaultStockAccId()))
                .isShortEntry(dto.getIsShortEntry())
                .isCashBill(dto.getIsCashBill())
                .printAfterInsert(dto.getPrintAfterInsert())
                .isBarcode(dto.getIsBarcode())
                .defaultCurrencyId(findCurrency(dto.getDefaultCurrencyId()))
                .build();
    }
    private void updateEntity(InvoiceType entity, UpdateInvoiceTypeRequest dto) {
        if (dto.getType() != null) entity.setType(Type.fromCode(dto.getType()));
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDefaultPriceId() != null) entity.setDefaultPriceId(findPrice(dto.getDefaultPriceId()));
        if (dto.getMinDefaultPriceId() != null) entity.setMinDefaultPriceId(findPrice(dto.getMinDefaultPriceId()));
        if (dto.getIsAffectCostPrice() != null) entity.setIsAffectCostPrice(dto.getIsAffectCostPrice());
        if (dto.getIsAffectLastPrice() != null) entity.setIsAffectLastPrice(dto.getIsAffectLastPrice());
        if (dto.getIsAffectCustPrice() != null) entity.setIsAffectCustPrice(dto.getIsAffectCustPrice());
        if (dto.getIsAffectProfit() != null) entity.setIsAffectProfit(dto.getIsAffectProfit());
        if (dto.getIsDiscAffectCost() != null) entity.setIsDiscAffectCost(dto.getIsDiscAffectCost());
        if (dto.getIsExtraAffectCost() != null) entity.setIsExtraAffectCost(dto.getIsExtraAffectCost());
        if (dto.getIsNoEntry() != null) entity.setIsNoEntry(dto.getIsNoEntry());
        if (dto.getIsAutoEntry() != null) entity.setIsAutoEntry(dto.getIsAutoEntry());
        if (dto.getIsAutoEntryPost() != null) entity.setIsAutoEntryPost(dto.getIsAutoEntryPost());
        if (dto.getIsNoPost() != null) entity.setIsNoPost(dto.getIsNoPost());
        if (dto.getIsAutoPost() != null) entity.setIsAutoPost(dto.getIsAutoPost());
        if (dto.getDefaultWarehouseId() != null) entity.setDefaultWarehouseId(findWarehouse(dto.getDefaultWarehouseId()));
        if (dto.getDefaultBillAccId() != null) entity.setDefaultBillAccId(findAccount(dto.getDefaultBillAccId()));
        if (dto.getDefaultCashAccId() != null) entity.setDefaultCashAccId(findAccount(dto.getDefaultCashAccId()));
        if (dto.getDefaultDiscAccId() != null) entity.setDefaultDiscAccId(findAccount(dto.getDefaultDiscAccId()));
        if (dto.getDefaultExtraAccId() != null) entity.setDefaultExtraAccId(findAccount(dto.getDefaultExtraAccId()));
        if (dto.getDefaultCostAccId() != null) entity.setDefaultCostAccId(findAccount(dto.getDefaultCostAccId()));
        if (dto.getDefaultStockAccId() != null) entity.setDefaultStockAccId(findAccount(dto.getDefaultStockAccId()));
        if (dto.getIsShortEntry() != null) entity.setIsShortEntry(dto.getIsShortEntry());
        if (dto.getIsCashBill() != null) entity.setIsCashBill(dto.getIsCashBill());
        if (dto.getPrintAfterInsert() != null) entity.setPrintAfterInsert(dto.getPrintAfterInsert());
        if (dto.getIsBarcode() != null) entity.setIsBarcode(dto.getIsBarcode());
        if (dto.getDefaultCurrencyId() != null) entity.setDefaultCurrencyId(findCurrency(dto.getDefaultCurrencyId()));
    }


    private InvoiceTypeResponse mapToResponse(InvoiceType entity) {
        return InvoiceTypeResponse.builder()
                .id(entity.getId())
                .type(String.valueOf(entity.getType()))
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
                .defaultBillAccId(entity.getDefaultBillAccId() != null ? entity.getDefaultBillAccId().getId() : null)
                .defaultCashAccId(entity.getDefaultCashAccId() != null ? entity.getDefaultCashAccId().getId() : null )
                .defaultDiscAccId(entity.getDefaultDiscAccId() != null ? entity.getDefaultDiscAccId().getId() : null)
                .defaultExtraAccId(entity.getDefaultExtraAccId() != null ? entity.getDefaultExtraAccId().getId() : null)
                .defaultCostAccId(entity.getDefaultCostAccId() != null ? entity.getDefaultCostAccId().getId() : null)
                .defaultStockAccId(entity.getDefaultStockAccId() != null ? entity.getDefaultStockAccId().getId() : null)
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
    private Account findAccount(Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found: " + id));
    }

}
