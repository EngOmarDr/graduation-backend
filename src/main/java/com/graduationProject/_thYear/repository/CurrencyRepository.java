package com.graduationProject._thYear.repository;

import com.graduationProject._thYear.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CurrencyRepository extends JpaRepository<Currency,Integer> {
    boolean existsByCode(String code);
    boolean existsByName(String name);
}
