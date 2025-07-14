package com.graduationProject._thYear.ProductStock.repositories;

import com.graduationProject._thYear.ProductStock.models.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductStockRepository extends JpaRepository<ProductStock, Integer> {
    Optional<ProductStock> findByProductIdAndWarehouseId(Integer productId, Integer warehouseId);
    Optional<ProductStock> findByProductIdAndWarehouseIdAndUnitItemId(Integer productId, Integer warehouseId , Integer unitItemId);
    List<ProductStock> findByWarehouseId(Integer warehouseId);
    List<ProductStock> findByProductId(Integer productId);
}
