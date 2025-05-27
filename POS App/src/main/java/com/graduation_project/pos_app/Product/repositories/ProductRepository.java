package com.graduation_project.pos_app.Product.repositories;

import com.graduation_project.pos_app.Product.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    boolean existsByCode(String code);
    boolean existsByName(String name);
}
