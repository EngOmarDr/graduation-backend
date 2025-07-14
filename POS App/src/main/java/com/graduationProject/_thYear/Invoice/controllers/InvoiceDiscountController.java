package com.graduationProject._thYear.Invoice.controllers;

import com.graduationProject._thYear.Invoice.dtos.requests.CreateInvoiceDiscountRequest;
import com.graduationProject._thYear.Invoice.dtos.requests.UpdateInvoiceDiscountRequest;
import com.graduationProject._thYear.Invoice.dtos.responses.InvoiceDiscountResponse;
import com.graduationProject._thYear.Invoice.services.InvoiceDiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice-discounts")
@RequiredArgsConstructor
public class InvoiceDiscountController {

    private final InvoiceDiscountService service;

    @PostMapping
    public ResponseEntity<InvoiceDiscountResponse> create(@Valid @RequestBody CreateInvoiceDiscountRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDiscountResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDiscountResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDiscountResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateInvoiceDiscountRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}