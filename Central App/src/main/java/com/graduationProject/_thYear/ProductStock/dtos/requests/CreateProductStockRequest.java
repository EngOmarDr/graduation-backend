package com.graduationProject._thYear.ProductStock.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductStockRequest {

    @NotNull
    private Integer productId;

    @NotNull
    private Integer warehouseId;

    @NotNull
    @PositiveOrZero
    private BigDecimal quantity;
}
