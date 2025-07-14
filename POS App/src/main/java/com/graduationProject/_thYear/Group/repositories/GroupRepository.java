package com.graduationProject._thYear.Group.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.graduationProject._thYear.Group.models.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group,Integer> {
    boolean existsByCode(String code);
    boolean existsByName(String name);

    List<Group> findByParentIsNull(); // For getting root categories

    List<Group> findByParentId(Integer parentId); // For getting children of a specific category

    @Query("SELECT g FROM Group g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(g.code) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Group> searchByNameOrCode(@Param("searchTerm") String searchTerm);
}
