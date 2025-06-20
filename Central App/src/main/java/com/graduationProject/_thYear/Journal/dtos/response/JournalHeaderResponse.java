package com.graduationProject._thYear.Journal.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.graduationProject._thYear.Branch.models.Branch;
import com.graduationProject._thYear.Currency.models.Currency;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class JournalHeaderResponse {
    private Integer id;
    private Integer branchId;
    private LocalDateTime date;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private Integer currencyId;
    private BigDecimal currencyValue;
    private Boolean isPosted;
    private Byte parentType;
}
