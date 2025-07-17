package com.graduationProject._thYear.Journal.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class UpdateJournalItemRequest {
    private Integer accountId;
    @PositiveOrZero(message = "Debit must be zero or positive")
    private BigDecimal debit;
    @PositiveOrZero(message = "Credit must be zero or positive")
    private BigDecimal credit;

    private Integer currencyId;

    private BigDecimal currencyValue;

    private LocalDateTime date;

    private String notes;
}
