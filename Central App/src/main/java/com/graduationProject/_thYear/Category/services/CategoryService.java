package com.graduationProject._thYear.Category.services;



import com.graduationProject._thYear.Category.dtos.request.CreateCategoryRequest;
import com.graduationProject._thYear.Category.dtos.request.UpdateCategoryRequest;
import com.graduationProject._thYear.Category.dtos.response.CategoryResponse;
import com.graduationProject._thYear.Category.dtos.response.CategoryTreeResponse;

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
