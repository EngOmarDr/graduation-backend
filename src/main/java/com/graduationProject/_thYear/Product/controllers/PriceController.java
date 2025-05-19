package com.graduationProject._thYear.Product.controllers;

import com.graduationProject._thYear.Product.dtos.request.CreatePriceRequest;
import com.graduationProject._thYear.Product.dtos.request.UpdatePriceRequest;
import com.graduationProject._thYear.Product.dtos.response.PriceResponse;
import com.graduationProject._thYear.Product.services.PriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {
    private final PriceService priceService;

    @PostMapping
    public ResponseEntity<PriceResponse> createPrice(
            @Valid @RequestBody CreatePriceRequest request) {
        PriceResponse response = priceService.createPrice(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceResponse> getPriceById(@PathVariable Integer id) {
        PriceResponse response = priceService.getPriceById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PriceResponse>> getAllPrices() {
        List<PriceResponse> responses = priceService.getAllPrices();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PriceResponse> updatePrice(
            @PathVariable Integer id,
            @Valid @RequestBody UpdatePriceRequest request) {
        PriceResponse response = priceService.updatePrice(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable Integer id) {
        priceService.deletePrice(id);
        return ResponseEntity.noContent().build();
    }
}
