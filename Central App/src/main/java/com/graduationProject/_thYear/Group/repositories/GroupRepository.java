package com.graduationProject._thYear.Group.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.graduationProject._thYear.Group.models.Group;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group,Integer> {
    boolean existsByCode(String code);
    boolean existsByName(String name);

    List<Group> findByParentIsNull(); // For getting root categories

    List<Group> findByParentId(Integer parentId); // For getting children of a specific category
}
