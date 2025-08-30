package com.graduationProject._thYear.InvoiceType.repositories;

import com.graduationProject._thYear.InvoiceType.models.InvoiceType;
import com.graduationProject._thYear.InvoiceType.models.Type;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceTypeRepository extends JpaRepository<InvoiceType,Integer> {
    boolean existsByType(Type type);

    Optional<InvoiceType> findByGlobalId(UUID globalId);
}
