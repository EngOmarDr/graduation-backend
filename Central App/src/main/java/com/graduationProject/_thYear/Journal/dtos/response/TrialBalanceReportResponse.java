package com.graduationProject._thYear.Journal.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Setter
@Getter
@SuperBuilder
public class TrialBalanceReportResponse  {

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private List<BalanceEntry> entries;

    @Setter
    @Getter
    @SuperBuilder
    public static class BalanceEntry {
        private Integer accountId; 
        private String accountCode;
        private String accountName;
        private BigDecimal debit;
        private BigDecimal credit;
    }
}