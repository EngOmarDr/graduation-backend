package com.graduationProject._thYear.InvoiceType.controllers;

import com.graduationProject._thYear.InvoiceType.dtos.requests.CreateInvoiceTypeRequest;
import com.graduationProject._thYear.InvoiceType.dtos.requests.UpdateInvoiceTypeRequest;
import com.graduationProject._thYear.InvoiceType.dtos.responses.InvoiceTypeResponse;
import com.graduationProject._thYear.InvoiceType.services.InvoiceTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice-types")
@RequiredArgsConstructor
public class InvoiceTypeController {


    private final InvoiceTypeService service;


    @PostMapping
    public ResponseEntity<InvoiceTypeResponse>  create(@RequestBody @Valid CreateInvoiceTypeRequest dto) {
        return ResponseEntity.ok(service.create( dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceTypeResponse>  getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));

    }

    @GetMapping
    public ResponseEntity<List<InvoiceTypeResponse>> getAll() {

        return ResponseEntity.ok(service.getAll());

    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceTypeResponse> update(@PathVariable Integer id, @RequestBody @Valid  UpdateInvoiceTypeRequest dto) {
        return ResponseEntity.ok(service.update(id, dto));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
