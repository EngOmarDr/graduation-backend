package com.graduationProject._thYear.Journal.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.graduationProject._thYear.Branch.models.Branch;
import com.graduationProject._thYear.Currency.models.Currency;

import com.graduationProject._thYear.Warehouse.models.Warehouse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "journalHeader")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JournalHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseId", nullable = false)
    private Warehouse warehouse;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Builder.Default
    @Column(name = "debit", nullable = false, precision = 19, scale = 4)
    private BigDecimal debit = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "credit", nullable = false, precision = 19, scale = 4)
    private BigDecimal credit = BigDecimal.ZERO;

    @OneToMany(mappedBy = "journalHeader", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<JournalItem> journalItems = new ArrayList<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyId", nullable = false)
    private Currency currency;

    @NotNull
    @Column(name = "currencyValue", nullable = false)
    private BigDecimal currencyValue;

    @Column(name = "parentType")
    private Byte parentType;

//    @Column(name = "parentId")
//    private Integer parentId;

    @Builder.Default
    @Column(name = "isPosted", nullable = false)
    private Boolean isPosted = false;

//    @Column(name = "postDate")
//    private LocalDateTime postDate;
//
//    @Column(name = "notes")
//    private String notes;


    public void addJournalItem(JournalItem journalItem) {
        journalItems.add(journalItem);
        journalItem.setJournalHeader(this);
    }
}