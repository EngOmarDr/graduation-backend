package com.graduationProject._thYear.ProductStock.dtos.responses;

import com.graduationProject._thYear.Product.dtos.response.ProductResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductWithStockResponse {
    // Product full details
    private ProductResponse product;

    // Stock details
    private Integer stockId;
    private Integer warehouseId;
    private String warehouseName;
    private Integer unitItemId;
    private String unitItemName;
    private BigDecimal quantity;
}
