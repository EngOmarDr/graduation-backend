package com.graduationProject._thYear.EventSyncronization.Records;

import java.util.UUID;

import com.graduationProject._thYear.Branch.models.Branch;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import com.graduationProject._thYear.Warehouse.models.WarehouseType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseRecord {
    private UUID globalId;
    private String name;
    private String code;
    private String phone;
    private String address;
    private BranchRecord branch;
    private WarehouseType type;
    private boolean isActive;
    private String notes;
    private WarehouseRecord parent;

    @Default
    private Boolean isDeleted = false;

    public static WarehouseRecord fromWarehouseEntity(Warehouse warehouse) {
        if(warehouse == null){
            return null;
        }
        return WarehouseRecord.builder()
            .globalId(warehouse.getGlobalId())
            .name(warehouse.getName())
            .code(warehouse.getCode())
            .address(warehouse.getAddress())
            .branch(BranchRecord.fromWarehouseEntity(warehouse.getBranch()))
            .type(warehouse.getType())
            .notes(warehouse.getNotes())
            .parent(fromWarehouseEntity(warehouse.getParent()))
            .build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BranchRecord{
        private UUID globalId;
        private String name;
        private String phone;
        private String address;
        private String notes;

        public static BranchRecord fromWarehouseEntity(Branch branch) {
            return BranchRecord.builder()
                .globalId(branch.getGlobalId())
                .name(branch.getName())
                .phone(branch.getPhone())
                .address(branch.getAddress())
                .notes(branch.getNotes())
                .build();
    }
    }
}
