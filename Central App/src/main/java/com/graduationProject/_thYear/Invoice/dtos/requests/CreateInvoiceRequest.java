package com.graduationProject._thYear.Invoice.dtos.requests;

import com.graduationProject._thYear.Invoice.annotationValidator.UniqueInvoiceItemProducts;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@UniqueInvoiceItemProducts
public class CreateInvoiceRequest {


    @NotNull(message = "warehouseId is required")
    private Integer warehouseId;
    @NotNull(message = "invoiceTypeId is required")
    private Integer invoiceTypeId;
    private LocalDateTime date;

    private Boolean isSuspended;

    @NotNull(message = "accountId is required")
    private Integer accountId;
    @NotNull(message = "currencyId is required")
    private Integer currencyId;

    @NotNull(message = "currencyValue is required")
    @Positive
    private BigDecimal currencyValue;
    @NotNull(message = "payType is required")
    private Integer payType;

    private Boolean isPosted = false;

    private LocalDateTime postedDate;
    private String notes;

    @Valid
    @NotNull(message = "invoiceItems are required")
    @NotEmpty(message = "At least one invoice item is required")
    private List<CreateInvoiceItemRequest> invoiceItems;

    private List<CreateInvoiceDiscountRequest> invoiceDiscounts;

}
