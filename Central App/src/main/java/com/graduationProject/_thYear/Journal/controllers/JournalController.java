package com.graduationProject._thYear.Journal.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.graduationProject._thYear.Journal.dtos.request.CreateJournalRequest;
import com.graduationProject._thYear.Journal.dtos.request.UpdateJournalRequest;
import com.graduationProject._thYear.Journal.dtos.response.JournalHeaderResponse;
import com.graduationProject._thYear.Journal.dtos.response.JournalResponse;
import com.graduationProject._thYear.Journal.dtos.response.LedgerReport;
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
    public ResponseEntity<List<JournalHeaderResponse>> getAllJournals() {
        var responses = service.getAllJournals();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<JournalHeaderResponse>> getJournalsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(service.getJournalsByDateRange(startDate, endDate));
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

    @GetMapping("/reports/ledger")
    public ResponseEntity<LedgerReport> getLedgerReport(
            @RequestParam Integer accountId,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        var report = service
                .generateLedgerReport(accountId, startDate, endDate);

        return ResponseEntity.ok(report);
    }

}
