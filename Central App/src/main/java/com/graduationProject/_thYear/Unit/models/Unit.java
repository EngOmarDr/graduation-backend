package com.graduationProject._thYear.Unit.models;

import com.graduationProject._thYear.Product.models.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "unit")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

     @Column(name = "globalId", nullable = false, updatable = false)
    @Default
    private UUID globalId = UUID.randomUUID();

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;
    
    @NotNull
    @Column(name = "name" , unique = true , nullable = false)
    private String name;

    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UnitItem> unitItems = new ArrayList<>();

//    @OneToMany(mappedBy = "priceUnit", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<ProductPrice> productPrices = new ArrayList<>();

    @OneToMany(mappedBy = "defaultUnit", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Product> products = new ArrayList<>();


    // Convenience method
    public void addUnitItem(UnitItem unitItem) {
        unitItems.add(unitItem);
        unitItem.setUnit(this);
    }
    public void removeUnitItem(UnitItem unitItem) {
        unitItems.remove(unitItem);
        unitItem.setUnit(null);
    }

    public void addProduct(Product product) {
        products.add(product);
        product.setDefaultUnit(this);
    }
    public void removeProduct(Product product) {
        products.remove(product);
        product.setDefaultUnit(null);
    }

}
