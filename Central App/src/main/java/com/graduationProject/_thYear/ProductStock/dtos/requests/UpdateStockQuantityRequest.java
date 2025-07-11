package com.graduationProject._thYear.ProductStock.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateStockQuantityRequest {

    @NotNull(message = "productId is required")
    private Integer productId;

    @NotNull(message = "warehouseId is required")
    private Integer warehouseId;

    @DecimalMin("0.0")
    @NotNull(message = "quantity is required")
    private BigDecimal quantity;
}
