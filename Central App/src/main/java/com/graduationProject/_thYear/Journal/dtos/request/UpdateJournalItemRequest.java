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
    @NotNull(message = "Account id is required")
    private Integer accountId;

    @PositiveOrZero(message = "Debit must be zero or positive")
    private BigDecimal debit;

    @PositiveOrZero(message = "Credit must be zero or positive")
    private BigDecimal credit;

    @NotNull(message = "Currency id is required")
    private Integer currencyId;

    @NotNull(message = "Currency value is required")
    private BigDecimal currencyValue;

    @NotNull(message = "Date is required")
    private LocalDateTime date;

    private String notes;
}
