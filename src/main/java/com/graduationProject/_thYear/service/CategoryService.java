package com.graduationProject._thYear.service;



import com.graduationProject._thYear.dto.request.CreateCategoryRequest;
import com.graduationProject._thYear.dto.request.UpdateCategoryRequest;
import com.graduationProject._thYear.dto.response.CategoryResponse;
import com.graduationProject._thYear.dto.response.CategoryTreeResponse;

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
