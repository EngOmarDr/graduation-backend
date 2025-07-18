package com.graduationProject._thYear.Warehouse.repositories;

import com.graduationProject._thYear.Warehouse.models.Warehouse;

import jakarta.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface  WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    Optional<Warehouse> findByName(String name);
    Optional<Warehouse> findByCode(String code);
    Optional<Warehouse> findByPhone(String phone);
    boolean existsByName(String name);
    boolean existsByCode(String code);
    boolean existsByPhone(String phone);
    boolean existsByBranchId(Integer branchId);
    List<Warehouse> findByParentIsNull(); // For getting root warehouses

    List<Warehouse> findByParentId(Integer parentId); // For getting children of a specific warehouse

    @Query("SELECT w FROM Warehouse w WHERE LOWER(w.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(w.code) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Warehouse> searchByNameOrCode(@Param("searchTerm") String searchTerm);

    @Query(
        "SELECT it.product.id as product_id, it.product.name as product_name, " + 
        "SUM(CASE WHEN ty.type IN ('buy','retrieve_sale','input') THEN it.qty ELSE 0 END) - SUM(CASE WHEN ty.type IN ('sale','retrieve_buy','ouput') THEN it.qty ELSE 0 END) as quantity " +
        "FROM Warehouse wh " +
        "JOIN InvoiceHeader ih ON wh.id = ih.warehouse.id " +
        "JOIN ih.invoiceItems it  " + 
        "JOIN ih.invoiceType ty " +
        "WHERE wh.id = :warehouseId " +
        "AND (:productId IS NULL OR it.product.id = (:productId)) " +
        "AND (:groupId IS NULL OR it.product.groupId.id = (:groupId)) " +
        "GROUP BY it.product"
    )
    List<Tuple> getStock(Integer warehouseId, Integer productId, Integer groupId);
}
