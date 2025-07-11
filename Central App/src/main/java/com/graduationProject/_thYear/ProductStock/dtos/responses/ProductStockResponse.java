package com.graduationProject._thYear.ProductStock.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductStockResponse {
    private Integer id;
    private Integer productId;
    private String productName;
    private Integer warehouseId;
    private String warehouseName;
    private BigDecimal quantity;
}
