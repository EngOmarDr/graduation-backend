package com.graduationProject._thYear.Warehouse.controllers;

import com.graduationProject._thYear.Group.dtos.response.GroupResponse;
import com.graduationProject._thYear.Group.dtos.response.GroupTreeResponse;
import com.graduationProject._thYear.Warehouse.dtos.requests.CreateWarehouseRequest;
import com.graduationProject._thYear.Warehouse.dtos.requests.UpdateWarehouseRequest;
import com.graduationProject._thYear.Warehouse.dtos.responses.WarehouseResponse;
import com.graduationProject._thYear.Warehouse.dtos.responses.WarehouseTreeResponse;
import com.graduationProject._thYear.Warehouse.services.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    public ResponseEntity<WarehouseResponse> create(@RequestBody @Valid CreateWarehouseRequest request) {
        return ResponseEntity.ok(warehouseService.createWarehouse(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponse> update(@PathVariable Integer id,
                                                       @RequestBody @Valid UpdateWarehouseRequest request) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Integer id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponse> getOne(@PathVariable Integer id) {
        return ResponseEntity.ok(warehouseService.getWarehouseById(id));
    }

    @GetMapping
    public ResponseEntity<List<WarehouseResponse>> getAll() {
        return ResponseEntity.ok(warehouseService.getAllWarehouses());
    }


    @GetMapping("/tree")
    public ResponseEntity<List<WarehouseTreeResponse>> getGroupTree() {
        List<WarehouseTreeResponse> tree = warehouseService.getWarehouseTree();
        return ResponseEntity.ok(tree);
    }

    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<WarehouseResponse>> getChildGroups(
            @PathVariable Integer parentId) {
        List<WarehouseResponse> responses =warehouseService.getChildWarehouses(parentId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<WarehouseResponse>> searchWarehouse(@RequestParam String q) {
        var responses = warehouseService.searchWarehouse(q);
        return ResponseEntity.ok(responses);
    }
}
