package com.graduationProject._thYear.Invoice.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateInvoiceItemRequest {

     private Integer invoiceHeaderId;

    private Integer productId;

    @PositiveOrZero
    @DecimalMin("0.0")
    private BigDecimal qty;

    @PositiveOrZero
    @DecimalMin("0.0")
    private BigDecimal price;

    @PositiveOrZero
    private BigDecimal bonusQty = BigDecimal.ZERO;

    private Integer unitItemId;

    @PositiveOrZero
    private BigDecimal unitFact;

    private String notes;
}
