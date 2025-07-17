package com.graduationProject._thYear.Transfer.controllers;

import com.graduationProject._thYear.Transfer.dtos.requests.CreateTransferRequest;
import com.graduationProject._thYear.Transfer.dtos.requests.UpdateTransferRequest;
import com.graduationProject._thYear.Transfer.dtos.responses.TransferResponse;
import com.graduationProject._thYear.Transfer.services.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService service;

    @PostMapping
    public ResponseEntity<TransferResponse> create(@Valid @RequestBody CreateTransferRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<TransferResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransferResponse> update(@PathVariable Integer id, @Valid @RequestBody UpdateTransferRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TransferResponse>> filterByWarehouseAndDate(
            @RequestParam(required = false) Integer warehouseId,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return ResponseEntity.ok(service.filterByWarehouseAndDate(warehouseId, start, end));
    }

}
