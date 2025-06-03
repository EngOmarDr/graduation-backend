package com.graduationProject._thYear.Journal.dtos.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateJournalRequest {
    // @NotNull(message = "Journal header is required")
    private CreateJournalHeaderRequest journalHeader;

    // @NotNull(message = "Journal items list is required")
    private List<CreateJournalItemRequest> journalItems;
}
