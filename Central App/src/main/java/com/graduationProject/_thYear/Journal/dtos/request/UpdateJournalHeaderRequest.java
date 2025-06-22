package com.graduationProject._thYear.Journal.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateJournalHeaderRequest {
    @NotNull(message = "Branch id is required")
    private Integer branchId;
//
//    @NotNull(message = "Date is required")
//    private LocalDateTime date;
//
//    @PositiveOrZero(message = "Debit must be zero or positive")
//    private BigDecimal debit;
//
//    @PositiveOrZero(message = "Credit must be zero or positive")
//    private BigDecimal credit;
//
//    @NotNull(message = "Currency id is required")
//    private Integer currencyId;
//
//    @NotNull(message = "Currency value is required")
//    private BigDecimal currencyValue;
//
//    private Byte parentType;
//    private Integer parentId;
//
//    @Builder.Default
//    private Boolean isPosted = true;
//
//    private LocalDateTime postDate;
//    private String notes;
}
