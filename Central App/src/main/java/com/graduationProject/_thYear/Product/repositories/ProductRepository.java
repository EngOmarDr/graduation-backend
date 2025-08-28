package com.graduationProject._thYear.Product.repositories;

import com.graduationProject._thYear.Invoice.models.InvoiceHeader;
import com.graduationProject._thYear.Product.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    boolean existsByCode(String code);
    boolean existsByName(String name);


    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.code) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> searchByNameOrCode(@Param("searchTerm") String searchTerm);


    
   @Query(value = """
                SELECT p FROM Product p
                WHERE p.createdAt > :date
           """)
    List<InvoiceHeader> findCreatedAfterDateTime(LocalDateTime date);

    @Query(value = """
                SELECT p FROM Product p
                WHERE p.updatedAt > :date
           """)
    List<InvoiceHeader> findUpdatedAfterDateTime(LocalDateTime date);


    @Query(value = """
                SELECT p FROM Product p
                WHERE p.deletedAt > :date
           """)
    List<InvoiceHeader> findDeletedAfterDateTime(LocalDateTime date);

}
