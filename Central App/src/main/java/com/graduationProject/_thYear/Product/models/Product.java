package com.graduationProject._thYear.Product.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.graduationProject._thYear.Group.models.Group;
import com.graduationProject._thYear.Unit.models.Unit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    public static final byte TYPE_WAREHOUSE = 0;  // Warehouse
    public static final byte TYPE_SERVICE = 1;    // Service

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "code" , unique = true, nullable = false)
    private String code;

    @NotNull
    @Column(name = "name" , unique = true ,nullable = false)
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group groupId;

    @Column(name = "image")
    private String image;

    @NotNull
    @Column(name = "type", nullable = false)
    private Byte type;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit defaultUnit;

    @NotNull
    @Column(name = "quantity" ,nullable = false)
    private Float quantity;

    @NotNull
    @Column(name = "minQty" ,nullable = false)
    private Float minQty;


    @NotNull
    @Column(name = "maxQty" ,nullable = false)
    private Float maxQty;

    @NotNull
    @Column(name = "orderQty" ,nullable = false)
    private Float orderQty;


    @Column(name = "notes" )
    private String notes;

    @OneToMany(mappedBy = "productId", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<ProductPrice> prices = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<ProductBarcode> barcodes = new ArrayList<>();

    // Convenience method
    public void addBarcode(ProductBarcode barcode) {
        barcodes.add(barcode);
        barcode.setProduct(this);
    }

    public void removeBarcode(ProductBarcode barcode) {
        barcodes.remove(barcode);
        barcode.setProduct(null);
    }

    public void addPrice(ProductPrice price) {
        prices.add(price);
        price.setProductId(this);
    }

    public void removePrice(ProductPrice price) {
        prices.remove(price);
        price.setProductId(null);
    }



}
