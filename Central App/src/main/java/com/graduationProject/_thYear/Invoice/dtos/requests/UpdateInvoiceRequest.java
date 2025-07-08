package com.graduationProject._thYear.Invoice.dtos.requests;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class UpdateInvoiceRequest {

    private Integer warehouseId;
    private Integer invoiceTypeId;
    private LocalDateTime date;
    private Boolean isSuspended;
    private Integer accountId;
    private Integer currencyId;
    @Positive
    private BigDecimal currencyValue;
    private Integer payType;
    private Boolean isPosted;
    private LocalDateTime postedDate;
    private String notes;

    private List<UpdateInvoiceItemRequest> invoiceItems;
    private List<UpdateInvoiceDiscountRequest> invoiceDiscounts;
}
