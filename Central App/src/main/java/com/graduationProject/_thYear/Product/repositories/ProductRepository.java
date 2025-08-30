package com.graduationProject._thYear.Product.repositories;

import com.graduationProject._thYear.Product.models.Product;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product,Integer> {
     boolean existsByCode(String code);
     boolean existsByName(String name);


     @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.code) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
     List<Product> searchByNameOrCode(@Param("searchTerm") String searchTerm);


     Slice<Product> findAllByCreatedAtAfter(LocalDateTime dateTime);
    
     @Query(value = """
                SELECT p FROM Product p
                WHERE :date IS null OR p.createdAt > :date
           """)
     Slice<Product> findCreatedAfterDateTime(LocalDateTime date, PageRequest pageRequest);

     @Query(value = """
                SELECT p FROM Product p
                WHERE p.updatedAt > :date
           """)
     Slice<Product> findUpdatedAfterDateTime(LocalDateTime date);


     @Query(value = """
                SELECT p FROM Product p
                WHERE p.deletedAt > :date
           """)
     Slice<Product> findDeletedAfterDateTime(LocalDateTime date);

     Optional<Product> findByGlobalId(UUID globalId);
}
