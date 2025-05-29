package com.graduationProject._thYear.Branch.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.graduationProject._thYear.Branch.dtos.request.CreateBranchRequest;
import com.graduationProject._thYear.Branch.dtos.request.UpdateBranchRequest;
import com.graduationProject._thYear.Branch.dtos.response.BranchResponse;
import com.graduationProject._thYear.Branch.services.BranchService;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {
    private final BranchService service;

    @PostMapping
    public ResponseEntity<BranchResponse> createProduct(
            @Valid @RequestBody CreateBranchRequest request) {
        var response = service.createBranch(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> getProductById(@PathVariable Integer id) {
        var response = service.getBranchById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BranchResponse>> getAllProducts() {
        var responses = service.getAllBranchs();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchResponse> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateBranchRequest request) {
        var response = service.updateBranch(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        service.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }
}
