package com.graduationProject._thYear.Currency.repositories;

import com.graduationProject._thYear.Currency.models.Currency;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CurrencyRepository extends JpaRepository<Currency,Integer> {
    boolean existsByCode(String code);
    boolean existsByName(String name);

    Optional<Currency> findByGlobalId(UUID globalId);

    @Query(value = """
                SELECT c FROM Currency c
                WHERE :date IS null OR c.createdAt > :date OR c.updatedAt > :date
           """)
     Slice<Currency> findAllByUpsertedAtAfter(LocalDateTime date, PageRequest pageRequest);

   

     @Query(value = """
                SELECT c FROM Currency c
                WHERE (:date IS null AND c.deletedAt IS NOT null) OR c.deletedAt > :date
           """)
     Slice<Currency> findAllByDeletetedAtAfter(LocalDateTime date);


}
