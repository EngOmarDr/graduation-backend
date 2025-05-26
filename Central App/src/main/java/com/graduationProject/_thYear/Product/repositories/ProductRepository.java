package com.graduationProject._thYear.Product.repositories;

import com.graduationProject._thYear.Product.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    boolean existsByCode(String code);
    boolean existsByName(String name);
}
