package com.graduationProject._thYear.Journal.services;

import java.time.LocalDate;
import java.util.List;

import com.graduationProject._thYear.Auth.models.User;
import com.graduationProject._thYear.EventSyncronization.Records.JournalRecord;
import com.graduationProject._thYear.Journal.dtos.request.CreateJournalRequest;
import com.graduationProject._thYear.Journal.dtos.request.UpdateJournalRequest;
import com.graduationProject._thYear.Journal.dtos.response.GeneralJournalReportResponse;
import com.graduationProject._thYear.Journal.dtos.response.JournalResponse;
import com.graduationProject._thYear.Journal.dtos.response.LedgerReport;
import com.graduationProject._thYear.Journal.dtos.response.TrialBalanceReportResponse;
import com.graduationProject._thYear.Journal.models.JournalHeader;

public interface JournalService {
    JournalResponse createJournal(CreateJournalRequest request);

    void setUser(User user);
    // JournalResponse getAccountById(Integer id);

    List<JournalResponse> listJournals(
        Integer branchId,
        Byte parentType,
        LocalDate startDate,
        LocalDate endDate
    );

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
        LocalDate startDate, 
        LocalDate endDate
    );

    List<JournalResponse> searchJournalsByParentType(Byte parentType);

    JournalHeader saveOrUpdate(JournalRecord journalRecord);
}
