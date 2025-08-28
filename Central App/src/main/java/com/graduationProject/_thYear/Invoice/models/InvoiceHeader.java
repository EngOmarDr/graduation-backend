package com.graduationProject._thYear.Invoice.models;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Auth.models.User;
import com.graduationProject._thYear.Currency.models.Currency;
import com.graduationProject._thYear.InvoiceType.models.InvoiceType;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "invoiceHeader")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;


    @Column(name = "globalId", nullable = false, updatable = false)
    @Default
    private UUID globalId = UUID.randomUUID();


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseId", nullable = false)
    private Warehouse warehouse;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceTypeId", nullable = false)
    private InvoiceType invoiceType;

    @NotNull
    @Column(name = "date",nullable = false)
    private LocalDateTime date;


    @NotNull
    @Builder.Default
    @Column(name = "isSuspended",nullable = false)
    private Boolean isSuspended = false ;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyId",nullable = false)
    private Currency currency;

    @NotNull
    @Column(name = "currencyValue",nullable = false)
    private BigDecimal currencyValue;

    @Column(name = "notes")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "parentType")
    private InvoiceKind parentType;

    @Column(name = "parentId")
    private Integer parentId;

    @NotNull
    @Builder.Default
    @Column(name = "total",nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @NotNull
    @Builder.Default
    @Column(name = "totalDisc",nullable = false)
    private BigDecimal totalDisc = BigDecimal.ZERO;

    @NotNull
    @Builder.Default
    @Column(name = "totalExtra",nullable = false)
    private BigDecimal totalExtra = BigDecimal.ZERO;

    @NotNull
    @Column(name = "payType",nullable = false)
    private Integer payType;

    @NotNull
    @Builder.Default
    @Column(name = "isPosted",nullable = false)
    private Boolean isPosted = false;

    @Column(name = "postedDate")
    private LocalDateTime postedDate;

    @NotNull
    @OneToMany(mappedBy = "invoiceHeader", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InvoiceItem> invoiceItems = new ArrayList<>();

    @OneToMany(mappedBy = "invoiceHeader", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InvoiceDiscount> invoiceDiscounts = new ArrayList<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



    @Column(name = "createdAt")
    private LocalDateTime createdAt;

      @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

      @Column(name = "deletedAt")
    private LocalDateTime deletedAt;

}
