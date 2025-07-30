package com.graduationProject._thYear.InvoiceTypePos.repositories;

import com.graduationProject._thYear.InvoiceTypePos.models.InvoicePos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoicePosRepository extends JpaRepository<InvoicePos, Integer> {
}
