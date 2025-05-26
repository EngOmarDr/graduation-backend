package com.graduationProject._thYear.Product.models;


import com.graduationProject._thYear.Unit.models.UnitItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productBarcode")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductBarcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id ;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_item_id", nullable = false)
    private UnitItem unitItem;

    @NotNull
    @Column(name = "barcode" ,nullable = false)
    private String barcode;
}
