package com.graduationProject._thYear.Journal.services;

import java.util.List;

import com.graduationProject._thYear.Journal.dtos.request.CreateJournalRequest;
import com.graduationProject._thYear.Journal.dtos.request.UpdateJournalRequest;
import com.graduationProject._thYear.Journal.dtos.response.JournalResponse;

public interface JournalService {
    JournalResponse createJournal(CreateJournalRequest request);

    // JournalResponse getAccountById(Integer id);

    List<JournalResponse> getAllJournals();

    JournalResponse updateJournal(Integer id, UpdateJournalRequest request);

    JournalResponse getJournalById(Integer id);

    void deleteJournal(Integer id);
}
