package com.graduationProject._thYear.Unit.controllers;

import com.graduationProject._thYear.Unit.dtos.requests.CreateUnitItemRequest;
import com.graduationProject._thYear.Unit.dtos.requests.UpdateUnitItemRequest;
import com.graduationProject._thYear.Unit.dtos.responses.UnitItemResponse;
import com.graduationProject._thYear.Unit.services.UnitItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/unit-items")
@RequiredArgsConstructor
public class UnitItemController {

    private final UnitItemService unitItemService;

    @PostMapping
    public ResponseEntity<UnitItemResponse> createUnitItem(
            @Valid @RequestBody CreateUnitItemRequest request) {
        UnitItemResponse response = unitItemService.createUnitItem(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitItemResponse> getUnitItemById(@PathVariable Integer id) {
        UnitItemResponse response = unitItemService.getUnitItemById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UnitItemResponse>> getAllUnitItems() {
        List<UnitItemResponse> responses = unitItemService.getAllUnitItems();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/unit/{unitId}")
    public ResponseEntity<List<UnitItemResponse>> getUnitItemsByUnitId(@PathVariable Integer unitId) {
        List<UnitItemResponse> responses = unitItemService.getUnitItemsByUnitId(unitId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnitItemResponse> updateUnitItem(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateUnitItemRequest request) {
        UnitItemResponse response = unitItemService.updateUnitItem(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnitItem(@PathVariable Integer id) {
        unitItemService.deleteUnitItem(id);
        return ResponseEntity.noContent().build();
    }
}
