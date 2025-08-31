package com.graduationProject._thYear.InvoiceType.repositories;

import com.graduationProject._thYear.InvoiceType.models.InvoiceType;
import com.graduationProject._thYear.InvoiceType.models.Type;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvoiceTypeRepository extends JpaRepository<InvoiceType,Integer> {
    boolean existsByType(Type type);

    Optional<InvoiceType> findByGlobalId(UUID globalId);


    @Query(value = """
                SELECT it FROM InvoiceType it
                WHERE :date IS null OR it.createdAt > :date OR it.updatedAt > :date
           """)
     Slice<InvoiceType> findAllByUpsertedAtAfter(LocalDateTime date, PageRequest pageRequest);

   

     @Query(value = """
                SELECT it FROM InvoiceType it
                WHERE it.deletedAt IS NOT null 
                AND (:date IS null ) OR ( it.deletedAt > :date)
           """)
     Slice<InvoiceType> findAllByDeletedAtAfter(LocalDateTime date, PageRequest pageRequest);
}
