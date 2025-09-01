package com.graduationProject._thYear.Journal.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.graduationProject._thYear.Branch.models.Branch;
import com.graduationProject._thYear.Currency.models.Currency;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "journalHeader")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JournalHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "globalId", nullable = false, updatable = false)
    @Default
    private UUID globalId = UUID.randomUUID();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branchId", nullable = false)
    private Branch branch;

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
    private Integer parentType;

    @Convert(converter = JournalKindConverter.class)
    @Column(name = "kind")
    private JournalKind kind;


    @Column(name = "parentId")
    private Integer parentId;
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

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

      @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

      @Column(name = "deletedAt")
    private LocalDateTime deletedAt;

      public void resetItems(List<JournalItem> items) {
          journalItems.clear();
          journalItems.addAll(items);
      }

}