package com.graduationProject._thYear.Invoice.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Data
@Builder
public class InvoiceResponse {
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

    private List<InvoiceItemResponse> invoiceItems;
    private List<InvoiceDiscountResponse> invoiceDiscounts;
}
