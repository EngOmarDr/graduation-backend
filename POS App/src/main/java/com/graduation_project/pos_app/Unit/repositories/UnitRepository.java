package com.graduation_project.pos_app.Unit.repositories;

import com.graduation_project.pos_app.Unit.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit,Integer> {
    boolean existsByName(String name);
}
