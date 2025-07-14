package com.graduationProject._thYear.Warehouse.dtos.requests;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.graduationProject._thYear.Warehouse.models.WarehouseType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWarehouseRequest {
    private String name;
    private String code;
    private String phone;
    private String address;
    private Integer branchId;
    private Integer parentId;
    private WarehouseType type;
    @JsonProperty("isActive")
    @JsonAlias({"isActive", "active"})
    private boolean isActive;
    private String notes;
}
