package com.graduationProject._thYear.JournalType.dtos.response;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Currency.models.Currency;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class JournalTypeResponse {
    private Short id;

    private String name;

    private Boolean autoPost;

    private Boolean fieldDebit;

    private Boolean fieldCredit;

    private Boolean fieldNotes;

    private Boolean fieldCurrencyName;

    private Boolean fieldDate;

    private Boolean fieldCurrencyEquilty;

    private Currency defaultCurrency;

    private String numberFormat;

    private String debitName;

    private String creditName;

    private Account defaultAccountId;
}
