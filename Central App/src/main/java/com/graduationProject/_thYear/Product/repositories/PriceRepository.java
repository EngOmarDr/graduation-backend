package com.graduationProject._thYear.Product.repositories;

import com.graduationProject._thYear.Product.models.Price;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price,Integer> {
    boolean existsByName(String name);



    Optional<Price> findByGlobalId(UUID globalId);

}
