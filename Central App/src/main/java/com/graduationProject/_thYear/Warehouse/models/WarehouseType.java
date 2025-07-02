package com.graduationProject._thYear.Warehouse.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.graduationProject._thYear.Auth.models.Role;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WarehouseType {
    WAREHOUSE,
    POS,

}
