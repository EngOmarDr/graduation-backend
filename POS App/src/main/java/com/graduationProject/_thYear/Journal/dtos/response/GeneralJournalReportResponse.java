package com.graduationProject._thYear.Journal.dtos.response;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class GeneralJournalReportResponse {

    @DateTimeFormat(iso = ISO.DATE)
    private Date date;
    private BigDecimal totalDebit;
    
    private BigDecimal totalCredit;

    private List<JournalEntry> entries;

    @Setter
    @Getter
    @SuperBuilder
    public static class JournalEntry {
        private Integer id;
        private Integer accountId; 
        private String accountCode;
        private String accountName;
        private BigDecimal debit;
        private BigDecimal credit;
        private String notes;
    }
}