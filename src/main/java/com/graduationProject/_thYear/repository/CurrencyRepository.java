package com.graduationProject._thYear.repository;

import com.graduationProject._thYear.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency,Integer> {
}
