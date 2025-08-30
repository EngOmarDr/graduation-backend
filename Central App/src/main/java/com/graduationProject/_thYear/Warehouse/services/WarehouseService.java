package com.graduationProject._thYear.Warehouse.services;

import com.graduationProject._thYear.EventSyncronization.Records.WarehouseRecord;
import com.graduationProject._thYear.Warehouse.dtos.requests.CreateWarehouseRequest;
import com.graduationProject._thYear.Warehouse.dtos.requests.UpdateWarehouseRequest;
import com.graduationProject._thYear.Warehouse.dtos.responses.WarehouseResponse;
import com.graduationProject._thYear.Warehouse.dtos.responses.WarehouseStockResponse;
import com.graduationProject._thYear.Warehouse.dtos.responses.WarehouseTreeResponse;
import com.graduationProject._thYear.Warehouse.models.Warehouse;

import java.util.List;

public interface WarehouseService {
    WarehouseResponse createWarehouse(CreateWarehouseRequest dto);
    WarehouseResponse updateWarehouse(Integer id, UpdateWarehouseRequest dto);
    void deleteWarehouse(Integer id);
    WarehouseResponse getWarehouseById(Integer id);
    List<WarehouseResponse> getAllWarehouses();

    List<WarehouseTreeResponse> getWarehouseTree();
    List<WarehouseResponse> getChildWarehouses(Integer parentId);

    List<WarehouseResponse> searchWarehouse(String searchTerm);

    WarehouseStockResponse getStock(Integer warehouseId, Integer productId, Integer groupId);
    Warehouse saveOrUpdate(WarehouseRecord branchRecord);

}
