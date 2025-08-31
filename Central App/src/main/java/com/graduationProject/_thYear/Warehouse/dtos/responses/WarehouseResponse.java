package com.graduationProject._thYear.Warehouse.dtos.responses;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.graduationProject._thYear.Warehouse.models.WarehouseType;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
public class WarehouseResponse {
    private Integer id;
    private UUID globalId;
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

}
