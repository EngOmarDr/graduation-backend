package com.graduation_project.pos_app.Currency.repositories;

import com.graduation_project.pos_app.Currency.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CurrencyRepository extends JpaRepository<Currency,Integer> {
    boolean existsByCode(String code);
    boolean existsByName(String name);
}
