package com.graduationProject._thYear.Warehouse.repositories;

import com.graduationProject._thYear.Group.models.Group;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
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
}
