package com.graduationProject._thYear.Invoice.controllers;
import com.graduationProject._thYear.Invoice.dtos.requests.CreateInvoiceItemRequest;
import com.graduationProject._thYear.Invoice.dtos.responses.InvoiceItemResponse;
import com.graduationProject._thYear.Invoice.dtos.requests.UpdateInvoiceItemRequest;
import com.graduationProject._thYear.Invoice.services.InvoiceItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/invoice-items")
@RequiredArgsConstructor
public class InvoiceItemController {

    private final InvoiceItemService service;

    @PostMapping
    public ResponseEntity<InvoiceItemResponse> create(@Valid @RequestBody CreateInvoiceItemRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceItemResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceItemResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceItemResponse> update(@PathVariable Integer id, @Valid @RequestBody UpdateInvoiceItemRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
