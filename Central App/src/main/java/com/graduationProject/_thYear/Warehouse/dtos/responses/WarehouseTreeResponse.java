package com.graduationProject._thYear.Warehouse.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.graduationProject._thYear.Warehouse.models.WarehouseType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
@Setter
@Getter
@SuperBuilder
public class WarehouseTreeResponse {
    private Integer id;
    private String name;
    private String code;
    private String phone;
    private String address;
    private Integer branchId;
    private String branchName;
    private WarehouseType type;
    @JsonProperty("isActive")
    private boolean isActive;
    private String notes;
    private Integer parentId;
    private String warehouseParentName;
    private List<WarehouseTreeResponse> children;
}
