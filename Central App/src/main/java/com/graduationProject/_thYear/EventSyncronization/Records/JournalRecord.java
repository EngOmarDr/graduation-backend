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
    private UUID branchId;
    private LocalDateTime date;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private UUID currencyId;
    private BigDecimal currencyValue;
    private Boolean isPosted;
    private Integer parentType;
    private Integer parentId;
    private JournalKind kind;
    private List<JournalItemRecord> journalItems;

    public static JournalRecord fromJournalEntity(JournalHeader journalHeader){
        System.out.println("inside journal record");
        System.out.println(journalHeader.getBranch().getGlobalId());
        System.out.println(journalHeader.getCurrencyValue());
        return JournalRecord.builder()
            .globalId(journalHeader.getGlobalId())
            .branchId(journalHeader.getBranch().getGlobalId())
            .date(journalHeader.getDate())
            .totalCredit(journalHeader.getCredit())
            .totalDebit(journalHeader.getDebit())
            .currencyId(journalHeader.getCurrency().getGlobalId())
            .isPosted(journalHeader.getIsPosted())
            .parentId(journalHeader.getParentId())
            .parentType(journalHeader.getParentType())
            .kind(journalHeader.getKind())
            .currencyValue(journalHeader.getCurrencyValue())
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
        private UUID accountId;
        private BigDecimal debit;
        private BigDecimal currencyValue;
        private BigDecimal credit;
        private UUID currencyId;
        private LocalDateTime date;
        private String notes;

        public static JournalItemRecord fromJournalItemEntity(JournalItem journalItem){
            return JournalItemRecord.builder()
                .globalId(journalItem.getGlobalId())
                .accountId(journalItem.getAccount().getGlobalId())
                .debit(journalItem.getDebit())
                .credit(journalItem.getCredit())
                .currencyId(journalItem.getCurrency().getGlobalId())
                .currencyValue(journalItem.getCurrencyValue())
                .date(journalItem.getDate())
                .notes(journalItem.getNotes())
                .build();
        }
    }
}


