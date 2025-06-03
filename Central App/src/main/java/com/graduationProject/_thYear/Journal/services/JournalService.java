package com.graduationProject._thYear.Journal.services;

import java.util.List;

import com.graduationProject._thYear.Journal.dtos.request.CreateJournalRequest;
import com.graduationProject._thYear.Journal.dtos.response.JournalItemResponse;
import com.graduationProject._thYear.Journal.dtos.response.JournalResponse;

public interface JournalService {
    JournalResponse createJournal(CreateJournalRequest request);

    // JournalResponse getAccountById(Integer id);

    List<JournalItemResponse> getAllJournals();

    // JournalResponse updateJournal(Integer id, UpdateJournalJournalRequest
    // request);

    void deleteJournal(Integer id);
}
