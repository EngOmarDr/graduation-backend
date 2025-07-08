package com.graduationProject._thYear.Invoice.repositories;

import com.graduationProject._thYear.Invoice.models.InvoiceHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceHeaderRepository extends JpaRepository<InvoiceHeader,Integer> {
    boolean existsByIdAndIsPosted(Integer id, Boolean isPosted);

}
