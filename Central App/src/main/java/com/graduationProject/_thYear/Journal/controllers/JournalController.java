package com.graduationProject._thYear.Journal.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.graduationProject._thYear.Journal.dtos.request.CreateJournalRequest;
import com.graduationProject._thYear.Journal.dtos.request.UpdateJournalRequest;
import com.graduationProject._thYear.Journal.dtos.response.JournalResponse;
import com.graduationProject._thYear.Journal.services.JournalService;

@RestController
@RequestMapping("/api/journals")
@RequiredArgsConstructor
public class JournalController {
    private final JournalService service;

    @PostMapping
    public ResponseEntity<JournalResponse> createJournal(
        @Valid @RequestBody CreateJournalRequest request) {
        var response = service.createJournal(request);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalResponse> getJournalById(@PathVariable Integer id) {
        JournalResponse response = service.getJournalById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<JournalResponse>> getAllJournals() {
        var responses = service.getAllJournals();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalResponse> updateJournal(
    @PathVariable Integer id,
    @Valid @RequestBody UpdateJournalRequest request) {
        JournalResponse response = service.updateJournal(id, request);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJournal(@PathVariable Integer id) {
        service.deleteJournal(id);
    return ResponseEntity.noContent().build();
    }

}
