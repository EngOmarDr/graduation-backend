package com.graduationProject._thYear.JournalType.services;

import java.util.List;

import com.graduationProject._thYear.JournalType.dtos.request.CreateJournalTypeRequest;
import com.graduationProject._thYear.JournalType.dtos.request.UpdateJournalTypeRequest;
import com.graduationProject._thYear.JournalType.dtos.response.JournalTypeResponse;

public interface JournalTypeService {
    JournalTypeResponse createJournalType(CreateJournalTypeRequest request);

    JournalTypeResponse getJournalTypeById(Short id);

    List<JournalTypeResponse> getAllJournalType();

    JournalTypeResponse updateJournalType(Short id, UpdateJournalTypeRequest request);

    void deleteJournalType(Short id);
}
