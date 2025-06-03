package com.graduationProject._thYear.JournalType.dtos.request;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Currency.models.Currency;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UpdateJournalTypeRequest {
    @NotNull(message = "Name is required")
    private String name;

    private Boolean autoPost;

    private Boolean fieldDebit;

    private Boolean fieldCredit;

    private Boolean fieldNotes;

    private Boolean fieldCurrencyName;

    private Boolean fieldCurrencyEquilty;

    private Currency defaultCurrency;

    private Boolean fieldDate;

    private String numberFormat;

    private String debitName;

    private String creditName;

    private Account defaultAccountId;
}
