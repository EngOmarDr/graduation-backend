package com.graduationProject._thYear.Purchase.repositories;

import com.graduationProject._thYear.Purchase.models.PurchaseHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseHeaderRepository extends JpaRepository<PurchaseHeader,Integer> {
}
