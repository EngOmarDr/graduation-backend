package com.graduationProject._thYear.Journal.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class LedgerReport {
    private Integer accountId;
    private String accountCode;
    private String accountName;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDateTime startDate;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDateTime endDate;

    private BigDecimal openingBalance;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private BigDecimal closingBalance;

    private List<LedgerEntry> entries;

    @Setter
    @Getter
    @SuperBuilder
    public static class LedgerEntry {
        private Integer id;
        private LocalDateTime date;
        private BigDecimal debit;
        private BigDecimal credit;
        private String notes;
        private Integer accountId;
        private String accountName;
        private String accountCode;
    }
}