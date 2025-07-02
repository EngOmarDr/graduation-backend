package com.graduationProject._thYear.Warehouse.dtos.requests;

import com.graduationProject._thYear.Warehouse.models.WarehouseType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWarehouseRequest {
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Code is required")
    private String code;

    @NotNull(message = "Phone is required")
    private String phone;

    @NotNull(message = "Address is required")
    private String address;

    @NotNull(message = "branchId is required")
    private Integer branchId;

    private Integer parentId;

    @NotNull(message = "Type is required")
    private WarehouseType type;

    private boolean isActive;

    private String notes;
}
