package com.graduationProject._thYear.Account.controllers;

import com.graduationProject._thYear.Account.dtos.request.CreateAccountRequest;
import com.graduationProject._thYear.Account.dtos.request.UpdateAccountRequest;
import com.graduationProject._thYear.Account.dtos.response.AccountResponse;
import com.graduationProject._thYear.Account.dtos.response.AccountTreeResponse;
import com.graduationProject._thYear.Account.services.AccountService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService aService;

    @PostMapping
    public ResponseEntity<AccountResponse> createProduct(
            @Valid @RequestBody CreateAccountRequest request) {
        var response = aService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getProductById(@PathVariable Integer id) {
        var response = aService.getAccountById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllProducts() {
        var responses = aService.getAllAccounts();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/tree")
    public ResponseEntity<List<AccountTreeResponse>> getGroupTree() {
        var tree = aService.getAccountsTree();
        return ResponseEntity.ok(tree);
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<List<AccountResponse>> getChildGroups(
            @PathVariable Integer id) {
        var responses = aService.getChildAccounts(id);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateAccountRequest request) {
        var response = aService.updateAccount(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        aService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public ResponseEntity<List<AccountResponse>> searchAccounts(@RequestParam String q) {
        var responses = aService.searchAccounts(q);
        return ResponseEntity.ok(responses);
    }
}
