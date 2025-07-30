package com.graduationProject._thYear.InvoiceTypePos.controllers;

import com.graduationProject._thYear.InvoiceTypePos.dtos.requests.InvoicePosCreateRequest;
import com.graduationProject._thYear.InvoiceTypePos.dtos.requests.InvoicePosUpdateRequest;
import com.graduationProject._thYear.InvoiceTypePos.dtos.responses.InvoicePosResponse;
import com.graduationProject._thYear.InvoiceTypePos.services.InvoicePosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice-pos")
@RequiredArgsConstructor
public class InvoicePosController {

    private final InvoicePosService invoicePosService;

    @PostMapping
    public ResponseEntity<InvoicePosResponse> create(@RequestBody InvoicePosCreateRequest request) {
        return ResponseEntity.ok(invoicePosService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoicePosResponse> update(@PathVariable Integer id,
                                                     @RequestBody InvoicePosUpdateRequest request) {
        return ResponseEntity.ok(invoicePosService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        invoicePosService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoicePosResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(invoicePosService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<InvoicePosResponse>> getAll() {
        return ResponseEntity.ok(invoicePosService.getAll());
    }
}
