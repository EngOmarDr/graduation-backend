package com.graduationProject._thYear.Invoice.dtos.requests;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateInvoiceDiscountRequest {

    private  Integer invoiceHeaderId;
    private Integer account;

    @PositiveOrZero
    @DecimalMax("100.00")
    private BigDecimal discount = BigDecimal.ZERO;

    @PositiveOrZero
    private BigDecimal extra = BigDecimal.ZERO;

    private String notes;
}
