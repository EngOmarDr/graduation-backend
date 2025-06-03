package com.graduationProject._thYear.JournalType.models;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Currency.models.Currency;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "journalType")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JournalType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Short id;

    @NotNull
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Builder.Default
    @Column(name = "autoPost")
    private Boolean autoPost = true;

    @Builder.Default
    @Column(name = "fieldDebit")
    private Boolean fieldDebit = false;

    @Builder.Default
    @Column(name = "fieldCredit")
    private Boolean fieldCredit = false;

    @Builder.Default
    @Column(name = "fieldNotes")
    private Boolean fieldNotes = false;

    @Builder.Default
    @Column(name = "fieldCurrencyName")
    private Boolean fieldCurrencyName = false;

    @Builder.Default
    @Column(name = "fieldCurrencyEquilty")
    private Boolean fieldCurrencyEquilty = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DefaultCurrency", nullable = true)
    private Currency DefaultCurrency;

    @Builder.Default
    @Column(name = "fieldDate")
    private Boolean fieldDate = false;

    @Column(name = "numberFormat", nullable = true)
    private String numberFormat;

    @Column(name = "debitName", nullable = true)
    private String debitName;

    @Column(name = "creditName", nullable = true)
    private String creditName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DefaultAccount", nullable = true)
    private Account defaultAccount;

}
