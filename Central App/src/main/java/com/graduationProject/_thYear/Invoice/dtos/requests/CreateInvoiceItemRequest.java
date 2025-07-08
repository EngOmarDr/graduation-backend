package com.graduationProject._thYear.Invoice.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateInvoiceItemRequest {


    private Integer invoiceHeaderId;

    @NotNull(message = "productId is required")
    private Integer productId;

    @PositiveOrZero
    @NotNull(message = "qty is required")
    @DecimalMin("0.0")
    private BigDecimal qty;

    @PositiveOrZero
    @NotNull(message = "price is required")
    @DecimalMin("0.0")
    private BigDecimal price;

    @PositiveOrZero
    private BigDecimal bonusQty = BigDecimal.ZERO;

    private Integer unitItemId;

    @PositiveOrZero
    private BigDecimal unitFact;

    private String notes;
}
