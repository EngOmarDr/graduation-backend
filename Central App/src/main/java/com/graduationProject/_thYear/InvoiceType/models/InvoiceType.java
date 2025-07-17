package com.graduationProject._thYear.InvoiceType.models;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Currency.models.Currency;
import com.graduationProject._thYear.Product.models.Price;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoiceType")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Type type; // buy - sale - retrieve_buy - retrieve_sale - input - output


    @Column(name = "name")
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defaultPriceId")
    private Price defaultPriceId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "minDefaultPriceId")
    private Price minDefaultPriceId;

    @Column(name = "isAffectCostPrice")
    private Boolean isAffectCostPrice;

    @Column(name = "isAffectLastPrice")
    private Boolean isAffectLastPrice;

    @Column(name = "isAffectCustPrice")
    private Boolean isAffectCustPrice;

    @Column(name = "isAffectProfit")
    private Boolean isAffectProfit;

    @Column(name = "isDiscAffectCost")
    private Boolean isDiscAffectCost;

    @Column(name = "isExtraAffectCost")
    private Boolean isExtraAffectCost;

    @Column(name = "isNoEntry")
    private Boolean isNoEntry;

    @Column(name = "isAutoEntry")
    private Boolean isAutoEntry;

    @Column(name = "isAutoEntryPost")
    private Boolean isAutoEntryPost;

    @Column(name = "isNoPost")
    private Boolean isNoPost;

    @Column(name = "isAutoPost")
    private Boolean isAutoPost;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defaultWarehouseId")
    private Warehouse defaultWarehouseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defaultBillAccId")
    private Account defaultBillAccId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defaultCashAccId")
    private Account defaultCashAccId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defaultDiscAccId")
    private Account defaultDiscAccId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defaultExtraAccId")
    private Account defaultExtraAccId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defaultCostAccId")
    private Account defaultCostAccId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defaultStockAccId")
    private Account defaultStockAccId;

    @Column(name = "isShortEntry")
    private Boolean isShortEntry;

    @Column(name = "isCashBill")
    private Boolean isCashBill;

    @Column(name = "printAfterInsert")
    private Boolean printAfterInsert;

    @Column(name = "isBarcode")
    private Boolean isBarcode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defaultCurrencyId")
    private Currency defaultCurrencyId;


    public boolean isStockIn() {
        return this.type == Type.buy || this.type == Type.input || this.type == Type.retrieve_sale;
    }

    public boolean isStockOut() {
        return this.type == Type.sale || this.type == Type.output || this.type == Type.retrieve_buy;
    }

}
