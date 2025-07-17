package com.graduationProject._thYear.Transfer.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransferItemResponse {
    private Integer id;
    private Integer productId;
    private String productName;
    private BigDecimal qty;
    private Integer unitItemId;
    private BigDecimal unitFact;
}
