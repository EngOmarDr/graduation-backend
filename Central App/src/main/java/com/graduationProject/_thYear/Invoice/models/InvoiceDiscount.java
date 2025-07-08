package com.graduationProject._thYear.Invoice.models;

import com.graduationProject._thYear.Account.models.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "invoiceDiscount")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceHeaderId", nullable = false)
    private InvoiceHeader invoiceHeader;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "extra")
    private BigDecimal extra;

    @Column(name = "notes")
    private String notes;
}
