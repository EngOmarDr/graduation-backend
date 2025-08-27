package com.graduationProject._thYear.Purchase.dtos.responses;

import com.graduationProject._thYear.Transfer.dtos.responses.TransferItemResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
public class PurchaseHeaderResponse {
    private Integer id;
    private Integer warehouseId;
    private String warehouseName;
    private LocalDateTime supplyDate;
    private LocalDateTime requestDate;
    private LocalDateTime buyDate;
    private LocalDateTime receiveDate;
    private String status;
    private String notes;
    private List<PurchaseItemResponse> items;
}
