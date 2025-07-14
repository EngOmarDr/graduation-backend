package com.graduationProject._thYear.Warehouse.dtos.requests;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    private String phone;

    private String address;

    @NotNull(message = "branchId is required")
    private Integer branchId;

    private Integer parentId;

    @NotNull(message = "Type is required")
    private WarehouseType type;

    @JsonProperty("isActive")
    @JsonAlias({"isActive", "active"})
    private boolean isActive;

    private String notes;
}
