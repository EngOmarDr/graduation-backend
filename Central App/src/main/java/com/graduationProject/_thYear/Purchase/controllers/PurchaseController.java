package com.graduationProject._thYear.Purchase.controllers;

import com.graduationProject._thYear.Purchase.dtos.requests.CreatePurchaseHeaderRequest;
import com.graduationProject._thYear.Purchase.dtos.requests.CreatePurchaseItemRequest;
import com.graduationProject._thYear.Purchase.dtos.requests.UpdatePurchaseHeaderRequest;
import com.graduationProject._thYear.Purchase.dtos.requests.UpdatePurchaseItemRequest;

import com.graduationProject._thYear.Purchase.dtos.responses.PurchaseHeaderResponse;
import com.graduationProject._thYear.Purchase.services.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<PurchaseHeaderResponse> create(@RequestBody CreatePurchaseHeaderRequest headerReq) {
        return ResponseEntity.ok(purchaseService.create(headerReq));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseHeaderResponse> update(@PathVariable Integer id,
                                                   @RequestBody UpdatePurchaseHeaderRequest headerReq) {
        return ResponseEntity.ok(purchaseService.update(id, headerReq));
    }

    @GetMapping
    public List<PurchaseHeaderResponse> getAll() {
        return purchaseService.getAll();
    }

    @GetMapping("/{id}")
    public PurchaseHeaderResponse getById(@PathVariable Integer id) {
        return purchaseService.getById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        purchaseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
