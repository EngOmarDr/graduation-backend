package com.graduationProject._thYear.Currency.controllers;

import com.graduationProject._thYear.Currency.dtos.requests.CreateCurrencyRequest;
import com.graduationProject._thYear.Currency.dtos.requests.UpdateCurrencyRequest;
import com.graduationProject._thYear.Currency.dtos.responses.CurrencyResponse;
import com.graduationProject._thYear.Currency.services.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @PostMapping
    public ResponseEntity<CurrencyResponse> createCurrency(
            @Valid @RequestBody CreateCurrencyRequest request) {
        CurrencyResponse response = currencyService.createCurrency(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyResponse> getCurrencyById(@PathVariable Integer id) {
        CurrencyResponse response = currencyService.getCurrencyById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CurrencyResponse>> getAllCurrencies() {
        List<CurrencyResponse> responses = currencyService.getAllCurrencies();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrencyResponse> updateCurrency(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCurrencyRequest request) {
        CurrencyResponse response = currencyService.updateCurrency(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Integer id) {
        currencyService.deleteCurrency(id);
        return ResponseEntity.noContent().build();
    }
}
