package com.graduation_project.pos_app.Unit.repositories;

import com.graduation_project.pos_app.Unit.models.UnitItem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnitItemRepository extends JpaRepository<UnitItem,Integer> {
    boolean existsByName(String name);
    List<UnitItem> findByUnitId(Integer unitId);
}
