package com.graduationProject._thYear.Unit.repositories;

import com.graduationProject._thYear.Unit.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit,Integer> {
    boolean existsByName(String name);
}
