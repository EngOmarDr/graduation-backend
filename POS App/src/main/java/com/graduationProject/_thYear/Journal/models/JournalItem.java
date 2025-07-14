package com.graduationProject._thYear.Journal.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Currency.models.Currency;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "journalItem", indexes = {
        @Index(name = "idx_journalitem_account", columnList = "account_id"),
        @Index(name = "idx_journalitem_date", columnList = "date"),
        @Index(name = "idx_journalitem_header_date", columnList = "journal_header_id,date")
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JournalItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_header_id", nullable = false)
    private JournalHeader journalHeader;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Builder.Default
    @Column(name = "debit", nullable = false, precision = 19, scale = 4)
    private BigDecimal debit = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "credit", nullable = false, precision = 19, scale = 4)
    private BigDecimal credit = BigDecimal.ZERO;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @NotNull
    @Column(name = "currency_value", nullable = false, precision = 19, scale = 4)
    private BigDecimal currencyValue;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "notes")
    private String notes;

    public String toString(){
        return "Journal Item: [" +
        this.getAccount().getName() + " " +
        this.getCredit() + " " +
        this.getDebit() + "]";
    }
}
