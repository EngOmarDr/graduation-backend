package com.graduationProject._thYear.Purchase.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class PurchaseItemResponse {
    private Integer id;
    private Integer productId;
    private String productName;
    private BigDecimal qty;
    private Integer unitItemId;
    private BigDecimal unitFact;
}
