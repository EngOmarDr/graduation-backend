package com.graduationProject._thYear.EventSyncronization.Records;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.graduationProject._thYear.Invoice.models.InvoiceDiscount;
import com.graduationProject._thYear.Invoice.models.InvoiceHeader;
import com.graduationProject._thYear.Invoice.models.InvoiceItem;
import com.graduationProject._thYear.Invoice.models.InvoiceKind;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceRecord {
    private UUID globalId;
    private UUID warehouseId;
    private UUID invoiceTypeId;
    private LocalDateTime date;
    private Boolean isSuspended;
    private UUID accountId;
    private UUID currencyId;
    private BigDecimal currencyValue;
    private BigDecimal total;
    private BigDecimal totalDisc;
    private BigDecimal totalExtra;
    private Integer payType;
    private Boolean isPosted;
    private InvoiceKind parentType;
    private Object parent;
    private LocalDateTime postedDate;
    private String notes;

    private List<InvoiceItemRecord> invoiceItems;
    private List<InvoiceDiscountRecord> invoiceDiscounts;   

    public static InvoiceRecord fromInvoiceEntity(InvoiceHeader invoiceHeader){
        return InvoiceRecord.builder()
            .globalId(invoiceHeader.getGlobalId())
            .accountId(invoiceHeader.getAccount().getGlobalId())
            .currencyId(invoiceHeader.getCurrency().getGlobalId())
            .currencyValue(invoiceHeader.getCurrencyValue())
            .total(invoiceHeader.getTotal())
            .totalDisc(invoiceHeader.getTotalDisc())
            .totalExtra(invoiceHeader.getTotalExtra())
            .parentType(invoiceHeader.getParentType())
            .payType(invoiceHeader.getPayType())
            .isPosted(invoiceHeader.getIsPosted())
            .isSuspended(invoiceHeader.getIsSuspended())
            .notes(invoiceHeader.getNotes())
            .postedDate(invoiceHeader.getPostedDate())
            .build();
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InvoiceItemRecord{
        private UUID globalId;
        private UUID productId;
        private BigDecimal qty;
        private BigDecimal price;
        private BigDecimal bonusQty;
        private UUID unitItemId;
        private BigDecimal unitFact;
        private String notes;

        public static InvoiceItemRecord fromInvoiceItemEntitgy(InvoiceItem invoiceItem){
            return InvoiceItemRecord.builder()
                .globalId(invoiceItem.getGlobalId())
                .productId(invoiceItem.getProduct().getGlobalId())
                .qty(invoiceItem.getQty())
                .price(invoiceItem.getPrice())
                .bonusQty(invoiceItem.getBonusQty())
                .unitItemId(invoiceItem.getUnitItem().getGlobalId())
                .unitFact(invoiceItem.getUnitFact())
                .notes(invoiceItem.getNotes())
                .build();
        }
        
    }

   
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InvoiceDiscountRecord{
        private UUID globalId;
        private UUID accountId;
        private BigDecimal discount;
        private BigDecimal extra;
        private String notes;

        public static InvoiceDiscountRecord fromInvoiceDiscountEntity(InvoiceDiscount invoiceDiscount){
            return InvoiceDiscountRecord.builder()
                .globalId(invoiceDiscount.getGlobalId())
                .accountId(invoiceDiscount.getAccount().getGlobalId())
                .discount(invoiceDiscount.getDiscount())
                .extra(invoiceDiscount.getExtra())
                .notes(invoiceDiscount.getNotes())
                .build();
        }
    }
}
