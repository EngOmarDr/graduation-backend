package com.graduationProject._thYear.Journal.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Currency.models.Currency;
import com.graduationProject._thYear.Journal.models.JournalHeader;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class JournalItemResponse {
    private Integer id;
    private Integer accountId;
    @Builder.Default
    private BigDecimal debit = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal credit = BigDecimal.ZERO;
    private Integer currencyId;
    private BigDecimal currencyValue;
    private LocalDateTime date;
}
