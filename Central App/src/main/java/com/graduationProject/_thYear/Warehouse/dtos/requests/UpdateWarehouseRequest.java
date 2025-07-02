package com.graduationProject._thYear.Warehouse.dtos.requests;

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
    private boolean isActive;
    private String notes;
}
