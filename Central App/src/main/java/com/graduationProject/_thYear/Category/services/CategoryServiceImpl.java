package com.graduationProject._thYear.Category.services;


import com.graduationProject._thYear.Category.dtos.request.CreateCategoryRequest;
import com.graduationProject._thYear.Category.dtos.request.UpdateCategoryRequest;
import com.graduationProject._thYear.Category.dtos.response.CategoryResponse;
import com.graduationProject._thYear.Category.dtos.response.CategoryTreeResponse;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import com.graduationProject._thYear.Category.models.Category;
import com.graduationProject._thYear.Category.repositories.CategoryRepository;
import com.graduationProject._thYear.Category.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {

// Validate name and code uniqueness
        if (categoryRepository.existsByCode(request.getName())) {
            throw new IllegalArgumentException("Category item with code '" + request.getCode() + "' already exists");
        }
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Category item with name '" + request.getName() + "' already exists");
        }

        Category category = new Category();
        category.setCode(request.getCode());
        category.setName(request.getName());
        category.setNotes(request.getNotes());

        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
            category.setParent(parent);
        }else {
            category.setParent(null);
        }

        Category savedCategory = categoryRepository.save(category);
        return convertToResponse(savedCategory);
    }



    @Override
    public CategoryResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return convertToResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public List<CategoryTreeResponse> getCategoryTree() {
        List<Category> rootCategories = categoryRepository.findByParentIsNull();
        return rootCategories.stream()
                .map(this::convertToTreeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getChildCategories(Integer parentId) {
        return categoryRepository.findByParentId(parentId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public CategoryResponse updateCategory(Integer id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.getCode().equals(request.getCode()) &&
                categoryRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Category item with code '" + request.getCode() + "' already exists");
        }

        if (!category.getName().equals(request.getName()) &&
                categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Category item with name '" + request.getName() + "' already exists");
        }

        category.setCode(request.getCode());
        category.setName(request.getName());
        category.setNotes(request.getNotes());

        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        Category updatedCategory = categoryRepository.save(category);
        return convertToResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.getChildren().isEmpty()) {
            category.getChildren().forEach(child -> {
                child.setParent(category.getParent());
                categoryRepository.save(child);
            });
        }

        categoryRepository.delete(category);
    }

    private CategoryResponse convertToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .code(category.getCode())
                .name(category.getName())
                .notes(category.getNotes())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .parentName(category.getParent() != null ? category.getParent().getName() : null)
                .build();
    }

    private CategoryTreeResponse convertToTreeResponse(Category category) {
        return CategoryTreeResponse.builder()
                .id(category.getId())
                .code(category.getCode())
                .name(category.getName())
                .notes(category.getNotes())
                .children(category.getChildren().stream()
                        .map(this::convertToTreeResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}
