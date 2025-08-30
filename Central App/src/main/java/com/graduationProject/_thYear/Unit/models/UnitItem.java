package com.graduationProject._thYear.Unit.models;


import com.graduationProject._thYear.Product.models.ProductBarcode;
import com.graduationProject._thYear.Product.models.ProductPrice;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "unitItem")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnitItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id ;

    
    @Column(name = "globalId", nullable = false, updatable = false)
    @Default
    private UUID globalId = UUID.randomUUID();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @NotNull
    @Column(name = "name" , unique = true ,nullable = false)
    private String name;

    @NotNull
    @Column(name = "fact" , nullable = false)
    private Float fact;

    @Column(name = "is_def" )
    @Default
    private Boolean isDef = false;

    @OneToMany(mappedBy = "unitItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductBarcode> barcodes = new ArrayList<>();


    @OneToMany(mappedBy = "priceUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductPrice> productPrices = new ArrayList<>();

    // Convenience method
    public void addBarcode(ProductBarcode barcode) {
        barcodes.add(barcode);
        barcode.setUnitItem(this);
    }

    public void removeBarcode(ProductBarcode barcode) {
        barcodes.remove(barcode);
        barcode.setUnitItem(null);
    }


    public void addProductPrice(ProductPrice productPrice) {
        productPrices.add(productPrice);
        productPrice.setPriceUnit(this);
    }

    public void removeProductPrice(ProductPrice productPrice) {
        productPrices.remove(productPrice);
        productPrice.setPriceUnit(null);
    }

}
