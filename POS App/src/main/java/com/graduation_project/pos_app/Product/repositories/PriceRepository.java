package com.graduation_project.pos_app.Product.repositories;

import com.graduation_project.pos_app.Product.models.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price,Integer> {
    boolean existsByName(String name);
}
