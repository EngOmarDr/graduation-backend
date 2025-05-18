package com.graduationProject._thYear.Category.repositories;

import com.graduationProject._thYear.Category.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    boolean existsByCode(String code);
    boolean existsByName(String name);

    List<Category> findByParentIsNull(); // For getting root categories

    List<Category> findByParentId(Integer parentId); // For getting children of a specific category
}
