package com.graduationProject._thYear.service;



import com.graduationProject._thYear.dto.request.category.CreateCategoryRequest;
import com.graduationProject._thYear.dto.request.category.UpdateCategoryRequest;
import com.graduationProject._thYear.dto.response.category.CategoryResponse;
import com.graduationProject._thYear.dto.response.category.CategoryTreeResponse;

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
