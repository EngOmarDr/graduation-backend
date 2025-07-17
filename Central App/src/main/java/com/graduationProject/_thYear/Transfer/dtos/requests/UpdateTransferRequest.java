package com.graduationProject._thYear.Transfer.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class UpdateTransferRequest {
    private Integer fromWarehouseId;

    private Integer toWarehouseId;

    private Integer cashAccountId;

    private Integer expenseAccountId;

    @DecimalMin(value = "0.00", inclusive = true, message = "Expense must be positive")
    private BigDecimal expenseValue;

    private LocalDateTime date;

    private String driverName;

    private String notes;

    private List<UpdateTransferItemRequest> items;
}
