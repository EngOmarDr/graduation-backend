package com.graduationProject._thYear.EventSyncronization.Records;

import java.util.Optional;
import java.util.UUID;

import com.graduationProject._thYear.InvoiceType.models.InvoiceType;
import com.graduationProject._thYear.InvoiceType.models.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceTypeRecord {
    private UUID globalId;
    private Type type;
    private String name;
    private UUID defaultPriceId;
    private UUID minDefaultPriceId;
    private Boolean isAffectCostPrice;
    private Boolean isAffectLastPrice;
    private Boolean isAffectCustPrice;
    private Boolean isAffectProfit;
    private Boolean isDiscAffectCost;
    private Boolean isExtraAffectCost;
    private Boolean isNoEntry;
    private Boolean isAutoEntry;
    private Boolean isAutoEntryPost;
    private Boolean isNoPost;
    private Boolean isAutoPost;
    private UUID defaultWarehouseId;
    private UUID defaultBillAccId;
    private UUID defaultCashAccId;
    private UUID defaultDiscAccId;
    private UUID defaultExtraAccId;
    private UUID defaultCostAccId;
    private UUID defaultStockAccId;
    private Boolean isShortEntry;
    private Boolean isCashBill;
    private Boolean printAfterInsert;
    private Boolean isBarcode;
    private UUID defaultCurrencyId;

    @Default
    private Boolean isDeleted = false;

    public static InvoiceTypeRecord fromInvoiceTypeEntity(InvoiceType invoiceType){
        return InvoiceTypeRecord.builder()
            .globalId(invoiceType.getGlobalId())
            .defaultBillAccId(invoiceType.getDefaultBillAccId().getGlobalId())
            .defaultCashAccId(invoiceType.getDefaultCashAccId().getGlobalId())
            .defaultDiscAccId(invoiceType.getDefaultDiscAccId().getGlobalId())
            .defaultExtraAccId(invoiceType.getDefaultExtraAccId().getGlobalId())
            .defaultCostAccId(invoiceType.getDefaultCostAccId().getGlobalId())
            .defaultStockAccId(invoiceType.getDefaultStockAccId().getGlobalId())
            .defaultWarehouseId(invoiceType.getDefaultWarehouseId().getGlobalId())
            .defaultCurrencyId(invoiceType.getDefaultCurrencyId().getGlobalId())
            .defaultPriceId(Optional.ofNullable(invoiceType.getDefaultPriceId())
                .map(price -> price.getGlobalId())
                .orElse(null))
            .minDefaultPriceId(Optional.ofNullable(invoiceType.getMinDefaultPriceId())
                .map(price -> price.getGlobalId())
                .orElse(null))
            .type(invoiceType.getType())
            .name(invoiceType.getName())
            .isAffectCostPrice(invoiceType.getIsAffectCostPrice())
            .isAffectLastPrice(invoiceType.getIsAffectLastPrice())
            .isAffectCustPrice(invoiceType.getIsAffectCustPrice())
            .isAffectProfit(invoiceType.getIsAffectProfit())
            .isDiscAffectCost(invoiceType.getIsDiscAffectCost())
            .isExtraAffectCost(invoiceType.getIsExtraAffectCost())
            .isNoEntry(invoiceType.getIsNoEntry())
            .isAutoEntry(invoiceType.getIsAutoEntry())
            .isAutoEntryPost(invoiceType.getIsAutoEntryPost())
            .isNoPost(invoiceType.getIsNoPost())
            .isAutoPost(invoiceType.getIsAutoPost())
            .isShortEntry(invoiceType.getIsShortEntry())
            .isCashBill(invoiceType.getIsCashBill())
            .printAfterInsert(invoiceType.getPrintAfterInsert())
            .isBarcode(invoiceType.getIsBarcode())
            .build();
    } 
}
