package com.graduation_project.pos_app.Category.services;



import com.graduation_project.pos_app.Category.dtos.request.CreateCategoryRequest;
import com.graduation_project.pos_app.Category.dtos.request.UpdateCategoryRequest;
import com.graduation_project.pos_app.Category.dtos.response.CategoryResponse;
import com.graduation_project.pos_app.Category.dtos.response.CategoryTreeResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CreateCategoryRequest request);
    CategoryResponse getCategoryById(Integer id);
    List<CategoryResponse> getAllCategories();
    List<CategoryTreeResponse> getCategoryTree();
    List<CategoryResponse> getChildCategories(Integer parentId);
    CategoryResponse updateCategory(Integer id, UpdateCategoryRequest request);
    void deleteCategory(Integer id);
}
