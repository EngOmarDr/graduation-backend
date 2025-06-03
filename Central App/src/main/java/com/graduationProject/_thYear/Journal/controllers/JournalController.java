package com.graduationProject._thYear.Journal.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.graduationProject._thYear.Journal.dtos.request.CreateJournalRequest;
import com.graduationProject._thYear.Journal.dtos.response.JournalItemResponse;
import com.graduationProject._thYear.Journal.dtos.response.JournalResponse;
import com.graduationProject._thYear.Journal.services.JournalService;

@RestController
@RequestMapping("/api/journals")
@RequiredArgsConstructor
public class JournalController {
    private final JournalService service;

    @PostMapping
    public ResponseEntity<Void> createJournal(
            @Valid @RequestBody CreateJournalRequest request) {
        service.createJournal(request);
        return ResponseEntity.noContent().build();
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<UnitResponse> getUnitById(@PathVariable Integer id) {
    // UnitResponse response = unitService.getUnitById(id);
    // return ResponseEntity.ok(response);
    // }

    @GetMapping
    public ResponseEntity<List<JournalItemResponse>> getAllJournals() {
        var responses = service.getAllJournals();
        return ResponseEntity.ok(responses);
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<UnitResponse> updateUnit(
    // @PathVariable Integer id,
    // @Valid @RequestBody UpdateUnitRequest request) {
    // UnitResponse response = unitService.updateUnit(id, request);
    // return ResponseEntity.ok(response);
    // }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteUnit(@PathVariable Integer id) {
    // unitService.deleteUnit(id);
    // return ResponseEntity.noContent().build();
    // }

}
