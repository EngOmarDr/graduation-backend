package com.graduationProject._thYear.Invoice.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class InvoiceItemResponse {
    private Integer id;
    private Integer invoiceHeaderId;
    private Integer productId;
    private String productName;
    private BigDecimal qty;
    private BigDecimal price;
    private BigDecimal bonusQty;
    private Integer unitItemId;
    private String unitItemName;
    private BigDecimal unitFact;
    private String notes;
}
