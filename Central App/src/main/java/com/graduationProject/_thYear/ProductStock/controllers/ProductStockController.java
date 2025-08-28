package com.graduationProject._thYear.ProductStock.controllers;

import com.graduationProject._thYear.ProductStock.dtos.requests.CreateProductStockRequest;
import com.graduationProject._thYear.ProductStock.dtos.requests.UpdateProductStockRequest;
import com.graduationProject._thYear.ProductStock.dtos.requests.UpdateStockQuantityRequest;
import com.graduationProject._thYear.ProductStock.dtos.responses.ProductStockResponse;
import com.graduationProject._thYear.ProductStock.dtos.responses.ProductWithStockResponse;
import com.graduationProject._thYear.ProductStock.services.ProductStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-stocks")
@RequiredArgsConstructor
public class ProductStockController {

    private final ProductStockService service;

    @PostMapping
    public ResponseEntity<ProductStockResponse> create(@RequestBody @Valid CreateProductStockRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @GetMapping
    public ResponseEntity<List<ProductStockResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductStockResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductStockResponse> update(@PathVariable Integer id, @RequestBody @Valid UpdateProductStockRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-warehouse/{warehouseId}")
    public ResponseEntity<List<ProductWithStockResponse>> getByWarehouse(@PathVariable Integer warehouseId) {
        return ResponseEntity.ok(service.getByWarehouseId(warehouseId));
    }

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<List<ProductStockResponse>> getByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(service.getByProductId(productId));
    }

    @PostMapping("/increase")
    public ResponseEntity<Void> increaseStock(@Valid @RequestBody UpdateStockQuantityRequest request) {
        service.increaseStock(
                request.getProductId(),
                request.getWarehouseId(),
                request.getUnitItemId(),
                request.getQuantity()
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/decrease")
    public ResponseEntity<Void> decreaseStock(@Valid @RequestBody UpdateStockQuantityRequest request) {
        service.decreaseStock(
                request.getProductId(),
                request.getWarehouseId(),
                request.getUnitItemId(),
                request.getQuantity()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/quantity")
    public ResponseEntity<ProductStockResponse> getQuantity(
            @RequestParam Integer productId,
            @RequestParam Integer warehouseId,
            @RequestParam Integer unitItemId) {
        return ResponseEntity.ok(service.getQuantity(productId, warehouseId, unitItemId));
    }
}
