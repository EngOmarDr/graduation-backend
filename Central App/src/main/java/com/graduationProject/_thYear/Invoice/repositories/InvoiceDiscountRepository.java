package com.graduationProject._thYear.Invoice.repositories;

import com.graduationProject._thYear.Invoice.models.InvoiceDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceDiscountRepository extends JpaRepository<InvoiceDiscount,Integer> {
}
