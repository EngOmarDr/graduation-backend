package com.graduationProject._thYear.Product.repositories;

import com.graduationProject._thYear.Product.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    boolean existsByCode(String code);
    boolean existsByName(String name);


    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.code) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> searchByNameOrCode(@Param("searchTerm") String searchTerm);
//
//    @Query("SELECT COUNT(p) FROM Product p")
//    Long getTotalProducts();
//
//    // (Optional) Low-stock products
//    @Query("SELECT p.name, p.minQty, p.maxQty " +
//            "FROM Product p " +
//            "WHERE p.minQty IS NOT NULL AND p.minQty > 0 " +
//            "AND (SELECT COALESCE(SUM(d.qty),0) FROM InvoiceDetail d WHERE d.product = p) < p.minQty")
//    List<Object[]> getLowStockProducts();

}
