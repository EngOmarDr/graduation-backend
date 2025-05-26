package com.graduationProject._thYear.Product.repositories;

import com.graduationProject._thYear.Product.models.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price,Integer> {
    boolean existsByName(String name);
}
