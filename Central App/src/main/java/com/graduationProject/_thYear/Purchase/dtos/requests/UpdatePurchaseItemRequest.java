package com.graduationProject._thYear.Purchase.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdatePurchaseItemRequest {

    private Integer productId;

    @DecimalMin(value = "0.01", inclusive = true, message = "Quantity must be positive")
    private BigDecimal qty;

    private Integer unitItemId;

    @DecimalMin(value = "0.00", inclusive = true, message = "Unit factor must be zero or positive")
    private BigDecimal unitFact;
}
