package com.graduationProject._thYear.Unit.repositories;

import com.graduationProject._thYear.Unit.models.Unit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit,Integer> {
    boolean existsByName(String name);


    @Query("SELECT CASE WHEN COUNT(ui) > 0 THEN true ELSE false END " +
            "FROM Unit u JOIN u.unitItems ui WHERE ui.name IN :names")
    boolean existsByUnitItemsNameIn(List<String> names);

    @Query("SELECT CASE WHEN COUNT(ui) > 0 THEN true ELSE false END " +
            "FROM Unit u JOIN u.unitItems ui WHERE ui.name IN :names AND u.id <> :unitId")
    boolean existsByUnitItemsNameInAndIdNot(List<String> names, Integer unitId);
        
    Optional<Unit> findByGlobalId(UUID globalId);

}
