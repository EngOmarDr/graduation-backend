package com.graduationProject._thYear.ProductStock.dtos.requests;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductStockRequest {

    private Integer productId;
    private Integer warehouseId;

    @PositiveOrZero
    private BigDecimal quantity;
}
