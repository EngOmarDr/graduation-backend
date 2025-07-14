package com.graduationProject._thYear.InvoiceType.dtos.requests;


import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceTypeRequest {
    private Integer type;
    private String name;
    private Integer defaultPriceId;
    private Integer minDefaultPriceId;
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
    private Integer defaultWarehouseId;
    private Integer defaultBillAccId;
    private Integer defaultCashAccId;
    private Integer defaultDiscAccId;
    private Integer defaultExtraAccId;
    private Integer defaultCostAccId;
    private Integer defaultStockAccId;
    private Boolean isShortEntry;
    private Boolean isCashBill;
    private Boolean printAfterInsert;
    private Boolean isBarcode;
    private Integer defaultCurrencyId;
}
