package com.graduationProject._thYear.Transfer.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class UpdateTransferItemRequest {

    private Integer productId;

    @DecimalMin(value = "0.01", inclusive = true, message = "Quantity must be positive")
    private BigDecimal qty;

    private Integer unitItemId;

    @DecimalMin(value = "0.00", inclusive = true, message = "Unit factor must be zero or positive")
    private BigDecimal unitFact;
}
