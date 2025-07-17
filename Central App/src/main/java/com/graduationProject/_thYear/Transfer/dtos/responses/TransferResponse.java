package com.graduationProject._thYear.Transfer.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TransferResponse {
    private Integer id;
    private Integer fromWarehouseId;
    private String fromWarehouseName;
    private Integer toWarehouseId;
    private String toWarehouseName;
    private Integer cashAccountId;
    private Integer expenseAccountId;
    private BigDecimal expenseValue;
    private LocalDateTime date;
    private String driverName;
    private String notes;
    private List<TransferItemResponse> items;
}
