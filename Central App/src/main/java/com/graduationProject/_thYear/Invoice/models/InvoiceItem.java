package com.graduationProject._thYear.Invoice.models;

import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Unit.models.UnitItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "InvoiceItem")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "globalId", nullable = false, updatable = false)
    @Default
    private UUID globalId = UUID.randomUUID();


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceHeaderId", nullable = false)
    private InvoiceHeader invoiceHeader;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @NotNull
    @Column(name = "qty",nullable = false)
    private BigDecimal qty;

    @NotNull
    @Column(name = "price",nullable = false)
    private BigDecimal price;

    @Column(name = "bonusQty")
    private BigDecimal bonusQty;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unitItemId")
    private UnitItem unitItem;

    @Column(name = "unitFact")
    private BigDecimal unitFact;


    private String notes;


}
