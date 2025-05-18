package com.graduationProject._thYear.Currency.repositories;

import com.graduationProject._thYear.Currency.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CurrencyRepository extends JpaRepository<Currency,Integer> {
    boolean existsByCode(String code);
    boolean existsByName(String name);
}
