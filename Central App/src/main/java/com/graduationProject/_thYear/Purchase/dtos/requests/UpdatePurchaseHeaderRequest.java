package com.graduationProject._thYear.Purchase.dtos.requests;

import com.graduationProject._thYear.Transfer.dtos.requests.CreateTransferItemRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdatePurchaseHeaderRequest {
    private Integer WarehouseId;

    private LocalDateTime supplyDate;

    private LocalDateTime requestDate;

    private LocalDateTime buyDate;

    private LocalDateTime receiveDate;

    private String notes;

    private Integer status;


    private List<UpdatePurchaseItemRequest> items;
}
