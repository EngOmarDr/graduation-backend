package com.graduationProject._thYear.Journal.controllers;

import com.graduationProject._thYear.Auth.models.Role;
import com.graduationProject._thYear.Auth.models.User;
import com.graduationProject._thYear.Branch.models.Branch;
import com.graduationProject._thYear.Journal.dtos.request.CreateJournalRequest;
import com.graduationProject._thYear.Journal.dtos.request.UpdateJournalRequest;
import com.graduationProject._thYear.Journal.dtos.response.GeneralJournalReportResponse;
import com.graduationProject._thYear.Journal.dtos.response.JournalResponse;
import com.graduationProject._thYear.Journal.dtos.response.LedgerReport;
import com.graduationProject._thYear.Journal.dtos.response.TrialBalanceReportResponse;
import com.graduationProject._thYear.Journal.services.JournalService;
import com.graduationProject._thYear.Warehouse.models.Warehouse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/journals")
public class JournalController {

    private final JournalService journalService;

    @PostMapping
    public ResponseEntity<JournalResponse> createJournal(@Valid @RequestBody CreateJournalRequest request) {
        JournalResponse response = journalService.createJournal(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<JournalResponse>> listJournals(
        @RequestParam(required = false) Integer branchId,
        @RequestParam(required = false) Byte parentType,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate,
        @AuthenticationPrincipal User user
    ) {
        journalService.setUser(user);
    
        List<JournalResponse> response = journalService.listJournals(branchId,parentType,startDate,endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<JournalResponse>> getJournalsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<JournalResponse> response = journalService.getJournalsByDateRange(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalResponse> getJournalById(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        journalService.setUser(user);
        JournalResponse response = journalService.getJournalById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalResponse> updateJournal(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateJournalRequest request,
            @AuthenticationPrincipal User user
        ) {
            journalService.setUser(user);
            JournalResponse response = journalService.updateJournal(id, request);
            return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
            
    public ResponseEntity<Void> deleteJournal(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        journalService.setUser(user);
        journalService.deleteJournal(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<JournalResponse>> searchJournals(
            @RequestParam(required = false) Byte parentType) {
        List<JournalResponse> response = journalService.searchJournalsByParentType(parentType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ledger-report")
    public ResponseEntity<LedgerReport> generateLedgerReport(
            @RequestParam Integer accountId,
            @RequestParam(required = false) Integer branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        LedgerReport response = journalService.generateLedgerReport(accountId, branchId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/general-journal-report")
    public ResponseEntity<List<GeneralJournalReportResponse>> generateGeneralJournalReport(
            @RequestParam(required = false) Integer branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<GeneralJournalReportResponse> response = journalService.generateGeneralJournalReport(branchId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/trial-balance-report")
    public ResponseEntity<TrialBalanceReportResponse> generateTrialBalanceReport(
            @RequestParam(required = false) Integer branchId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
            ) {
        TrialBalanceReportResponse response = journalService.generateTrialBalanceReport(branchId, startDate, endDate);
        return ResponseEntity.ok(response);
    }
}