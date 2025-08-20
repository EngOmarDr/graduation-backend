package com.graduationProject._thYear.ProductStock.dtos.responses;

import com.graduationProject._thYear.Product.dtos.response.ProductResponse;
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
    private Integer unitItemId;
    private String unitItemName;
    private BigDecimal quantity;
}
