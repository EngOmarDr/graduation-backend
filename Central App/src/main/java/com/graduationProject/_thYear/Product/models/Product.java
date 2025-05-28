package com.graduationProject._thYear.Product.models;

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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit defaultUnit;

    @NotNull
    @Column(name = "low_qty" ,nullable = false)
    private Float lowQty;


    @OneToMany(mappedBy = "productId", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductPrice> prices = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
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
