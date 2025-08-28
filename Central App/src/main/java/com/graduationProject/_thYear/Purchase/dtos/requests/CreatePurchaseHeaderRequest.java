package com.graduationProject._thYear.Purchase.dtos.requests;

import com.graduationProject._thYear.Transfer.dtos.requests.CreateTransferItemRequest;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreatePurchaseHeaderRequest {
    @NotNull
    private Integer WarehouseId;

    private LocalDateTime supplyDate;

    private LocalDateTime requestDate;

    private LocalDateTime buyDate;

    private LocalDateTime receiveDate;

    private String notes;

    private BigDecimal total;

    private String supplierName;

    @NotNull
    private List<CreatePurchaseItemRequest> items;
}
