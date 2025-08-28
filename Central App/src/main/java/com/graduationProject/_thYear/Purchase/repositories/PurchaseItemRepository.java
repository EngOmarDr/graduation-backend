package com.graduationProject._thYear.Purchase.repositories;

import com.graduationProject._thYear.Purchase.models.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem,Integer> {
}
