package com.graduationProject._thYear.EventSyncronization.Records;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.graduationProject._thYear.EventSyncronization.Records.WarehouseRecord.BranchRecord;
import com.graduationProject._thYear.Journal.dtos.response.JournalItemResponse;
import com.graduationProject._thYear.Journal.models.JournalHeader;
import com.graduationProject._thYear.Journal.models.JournalItem;
import com.graduationProject._thYear.Journal.models.JournalKind;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JournalRecord {

    
    private UUID globalId;
    private BranchRecord branch;
    private LocalDateTime date;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private CurrencyRecord currency;
    private Boolean isPosted;
    private Integer parentType;
    private Integer parentId;
    private JournalKind kind;
    private List<JournalItemRecord> journalItems;

    public static JournalRecord fromJournalEntity(JournalHeader journalHeader){
        return JournalRecord.builder()
            .globalId(journalHeader.getGlobalId())
            .branch(BranchRecord.fromBranchEntity(journalHeader.getBranch()))
            .date(journalHeader.getDate())
            .totalCredit(journalHeader.getCredit())
            .totalDebit(journalHeader.getDebit())
            .currency(CurrencyRecord.fromCurrencyEntity(journalHeader.getCurrency()))
            .isPosted(journalHeader.getIsPosted())
            .parentId(journalHeader.getParentId())
            .parentType(journalHeader.getParentType())
            .kind(journalHeader.getKind())
            .journalItems(journalHeader.getJournalItems().stream()
                .map(item -> JournalItemRecord.fromJournalItemEntity(item))
                .collect(Collectors.toList()))
            .build();
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class JournalItemRecord{
        private UUID globalId;
        private AccountRecord account;
        private BigDecimal debit;
        private BigDecimal credit;
        private CurrencyRecord currency;
        private LocalDateTime date;
        private String notes;

        public static JournalItemRecord fromJournalItemEntity(JournalItem journalItem){
            return JournalItemRecord.builder()
                .globalId(journalItem.getGlobalId())
                .account(AccountRecord.fromAccountEntity(journalItem.getAccount()))
                .debit(journalItem.getDebit())
                .credit(journalItem.getCredit())
                .currency(CurrencyRecord.fromCurrencyEntity(journalItem.getCurrency()))
                .date(journalItem.getDate())
                .notes(journalItem.getNotes())
                .build();
        }
    }
}


