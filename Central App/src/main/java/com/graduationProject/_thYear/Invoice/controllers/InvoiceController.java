package com.graduationProject._thYear.Invoice.controllers;

import com.graduationProject._thYear.Invoice.dtos.requests.*;
import com.graduationProject._thYear.Invoice.dtos.responses.*;
import com.graduationProject._thYear.Invoice.services.InvoiceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService service;

    @PostMapping
    public ResponseEntity<InvoiceResponse> create(@Valid @RequestBody CreateInvoiceRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

//
   @GetMapping("/material-movement-report")
   public List<MaterialMovementResponse> reportMaterialMovement(
           @RequestParam LocalDate startDate,
           @RequestParam LocalDate endDate,
           @RequestParam(required = false) Integer productId,
           @RequestParam(required = false) Integer groupId,
           @RequestParam(required = false) Integer warehouseId
       ) {
       var response = service.reportMaterialMovement(startDate, endDate, productId, groupId, warehouseId);
       return response;
   }

   @GetMapping("/daily-movement-report")
   public DailyMovementResponse reportDailyMovement(
           @RequestParam LocalDate startDate,
           @RequestParam LocalDate endDate,
           @RequestParam(required = false) Integer productId,
           @RequestParam(required = false) Integer groupId,
           @RequestParam(required = false) Integer warehouseId
       ) {
       var response = service.reportDailyMovement(startDate, endDate, productId, groupId, warehouseId);
       return response;
   }

   @GetMapping("/product-stock-report")
   public ProductStockResponse reportProductStock(
           @RequestParam LocalDate startDate,
           @RequestParam LocalDate endDate,
           @RequestParam(required = false) Integer productId,
           @RequestParam(required = false) Integer groupId,
           @RequestParam(required = false) Integer warehouseId
       ) {
       var response = service.reportProductStock(startDate, endDate, productId, groupId, warehouseId);
       return response;
   }
    
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> update(@PathVariable Integer id, @RequestBody @Valid UpdateInvoiceRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
