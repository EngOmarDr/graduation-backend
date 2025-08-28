package com.graduationProject._thYear.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRecord {

    private Integer id;
    private Integer warehouseId;
    private String warehouseName;
    private Integer invoiceTypeId;
    private String invoiceTypeName;
    private LocalDateTime date;
    private Boolean isSuspended;
    private Integer accountId;
    private Integer currencyId;
    private BigDecimal currencyValue;
    private BigDecimal total;
    private BigDecimal totalDisc;
    private BigDecimal totalExtra;
    private Integer payType;
    private Boolean isPosted;
    private LocalDateTime postedDate;
    private String notes;

    private List<InvoiceItemRecord> invoiceItems;
    private List<InvoiceDiscountRecord> invoiceDiscounts;


    @Data
    @Builder
    public static class InvoiceItemRecord {
        private Integer id;
        private Integer invoiceHeaderId;
        private Integer productId;
        private String productName;
        private BigDecimal qty;
        private BigDecimal price;
        private BigDecimal bonusQty;
        private Integer unitItemId;
        private String unitItemName;
        private BigDecimal unitFact;
        private String notes;
    }


    @Data
    @Builder
    public static class InvoiceDiscountRecord {
        private Integer id;
        private Integer invoiceHeaderId;
        private Integer account;
        private BigDecimal discount;
        private BigDecimal extra;
        private String notes;
    }

}
