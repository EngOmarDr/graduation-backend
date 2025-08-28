package com.graduationProject._thYear.Purchase.repositories;

import com.graduationProject._thYear.Purchase.models.PurchaseHeader;
import com.graduationProject._thYear.Purchase.models.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseHeaderRepository extends JpaRepository<PurchaseHeader,Integer> {
    List<PurchaseHeader> findByStatus(StatusType status);
}
