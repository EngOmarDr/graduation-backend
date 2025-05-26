package com.graduationProject._thYear.Unit.models;


import com.graduationProject._thYear.Product.models.ProductBarcode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "unitItem")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnitItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id ;

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
    private Boolean isDef;

    @OneToMany(mappedBy = "unitItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductBarcode> barcodes = new ArrayList<>();

    // Convenience method
    public void addBarcode(ProductBarcode barcode) {
        barcodes.add(barcode);
        barcode.setUnitItem(this);
    }

    public void removeBarcode(ProductBarcode barcode) {
        barcodes.remove(barcode);
        barcode.setUnitItem(null);
    }

}
