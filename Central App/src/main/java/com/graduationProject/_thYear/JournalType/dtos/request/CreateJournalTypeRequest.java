package com.graduationProject._thYear.JournalType.dtos.request;

import com.graduationProject._thYear.Currency.models.Currency;

import jakarta.validation.constraints.NotNull;
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
public class CreateJournalTypeRequest {

    @NotNull(message = "Name is required")
    private String name;

    @Builder.Default
    private Boolean autoPost = false;

    @Builder.Default
    private Boolean fieldDebit = false;

    @Builder.Default
    private Boolean fieldCredit = false;

    @Builder.Default
    private Boolean fieldNotes = false;

    @Builder.Default
    private Boolean fieldCurrencyName = false;

    @Builder.Default
    private Boolean fieldCurrencyEquilty = false;

    private Currency defaultCurrency;

    @Builder.Default
    private Boolean fieldDate = false;

    private String numberFormat;

    private String debitName;

    private String creditName;

    private Integer DefaultAccountId;

}
