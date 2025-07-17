package com.graduationProject._thYear.Journal.services;

import java.time.LocalDate;
import java.util.List;

import com.graduationProject._thYear.Journal.dtos.request.CreateJournalRequest;
import com.graduationProject._thYear.Journal.dtos.request.UpdateJournalRequest;
import com.graduationProject._thYear.Journal.dtos.response.GeneralJournalReportResponse;
import com.graduationProject._thYear.Journal.dtos.response.JournalResponse;
import com.graduationProject._thYear.Journal.dtos.response.LedgerReport;
import com.graduationProject._thYear.Journal.dtos.response.TrialBalanceReportResponse;

public interface JournalService {
    JournalResponse createJournal(CreateJournalRequest request);

    // JournalResponse getAccountById(Integer id);

    List<JournalResponse> getAllJournals();

    List<JournalResponse> getJournalsByDateRange(LocalDate startDate, LocalDate endDate);

    JournalResponse updateJournal(Integer id, UpdateJournalRequest request);

    JournalResponse getJournalById(Integer id);

    void deleteJournal(Integer id);

    LedgerReport generateLedgerReport(
        Integer accountId,
        Integer branchId,
        LocalDate startDate,
        LocalDate endDate
    );

     List<GeneralJournalReportResponse> generateGeneralJournalReport(
        Integer branchId,
        LocalDate startDate,
        LocalDate endDate
    );

    TrialBalanceReportResponse generateTrialBalanceReport(
        Integer branchId,
        LocalDate date
    );

    List<JournalResponse> searchJournalsByParentType(Byte parentType);
}
