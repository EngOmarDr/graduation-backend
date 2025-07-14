package com.graduationProject._thYear.Invoice.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InvoiceDiscountResponse {
    private Integer id;
    private Integer invoiceHeaderId;
    private Integer account;
    private BigDecimal discount;
    private BigDecimal extra;
    private String notes;
}
