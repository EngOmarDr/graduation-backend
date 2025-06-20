package com.graduationProject._thYear.Journal.dtos.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class JournalResponse {
    private JournalHeaderResponse journalHeader;
    private List<JournalItemResponse> journalItems;
}
