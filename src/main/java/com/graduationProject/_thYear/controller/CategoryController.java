package com.graduationProject._thYear.controller;


import com.graduationProject._thYear.dto.request.category.CreateCategoryRequest;
import com.graduationProject._thYear.dto.request.category.UpdateCategoryRequest;
import com.graduationProject._thYear.dto.response.category.CategoryResponse;
import com.graduationProject._thYear.dto.response.category.CategoryTreeResponse;
import com.graduationProject._thYear.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer categoryId) {
        CategoryResponse response = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> responses = categoryService.getAllCategories();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/tree")
    public ResponseEntity<List<CategoryTreeResponse>> getCategoryTree() {
        List<CategoryTreeResponse> tree = categoryService.getCategoryTree();
        return ResponseEntity.ok(tree);
    }

    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<CategoryResponse>> getChildCategories(
            @PathVariable Integer parentId) {
        List<CategoryResponse> responses = categoryService.getChildCategories(parentId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Integer categoryId,
            @Valid @RequestBody UpdateCategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

}
