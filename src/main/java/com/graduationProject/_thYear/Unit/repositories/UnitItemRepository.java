package com.graduationProject._thYear.Unit.repositories;

import com.graduationProject._thYear.Unit.models.UnitItem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnitItemRepository extends JpaRepository<UnitItem,Integer> {
    boolean existsByName(String name);
    List<UnitItem> findByUnitId(Integer unitId);
}
