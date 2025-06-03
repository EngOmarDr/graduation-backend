package com.graduationProject._thYear.JournalType.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.graduationProject._thYear.JournalType.dtos.request.CreateJournalTypeRequest;
import com.graduationProject._thYear.JournalType.dtos.request.UpdateJournalTypeRequest;
import com.graduationProject._thYear.JournalType.dtos.response.JournalTypeResponse;
import com.graduationProject._thYear.JournalType.services.JournalTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/journalTypes")
@RequiredArgsConstructor
public class JournalTypeController {
    private final JournalTypeService service;

    @PostMapping
    public ResponseEntity<JournalTypeResponse> createJournalType(
            @Valid @RequestBody CreateJournalTypeRequest request) {
        var response = service.createJournalType(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{journalTypeId}")
    public ResponseEntity<JournalTypeResponse> getJournalTypeById(@PathVariable Short journalTypeId) {
        var response = service.getJournalTypeById(journalTypeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<JournalTypeResponse>> getAllJournalTypes() {
        var responses = service.getAllJournalType();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{journalTypeId}")
    public ResponseEntity<JournalTypeResponse> updateJournalType(
            @PathVariable Short journalTypeId,
            @Valid @RequestBody UpdateJournalTypeRequest request) {
        var response = service.updateJournalType(journalTypeId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{journalTypeId}")
    public ResponseEntity<Void> deleteJournalType(@PathVariable Short journalTypeId) {
        service.deleteJournalType(journalTypeId);
        return ResponseEntity.noContent().build();
    }

}
