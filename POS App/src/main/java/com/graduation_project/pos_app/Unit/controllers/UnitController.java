package com.graduation_project.pos_app.Unit.controllers;
import com.graduation_project.pos_app.Unit.dtos.requests.CreateUnitRequest;
import com.graduation_project.pos_app.Unit.dtos.requests.UpdateUnitRequest;
import com.graduation_project.pos_app.Unit.dtos.responses.UnitResponse;
import com.graduation_project.pos_app.Unit.services.UnitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class UnitController {
    private final UnitService unitService;

    @PostMapping
    public ResponseEntity<UnitResponse> createUnit(
            @Valid @RequestBody CreateUnitRequest request) {
        UnitResponse response = unitService.createUnit(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitResponse> getUnitById(@PathVariable Integer id) {
        UnitResponse response = unitService.getUnitById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UnitResponse>> getAllUnits() {
        List<UnitResponse> responses = unitService.getAllUnits();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnitResponse> updateUnit(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateUnitRequest request) {
        UnitResponse response = unitService.updateUnit(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Integer id) {
        unitService.deleteUnit(id);
        return ResponseEntity.noContent().build();
    }

}
