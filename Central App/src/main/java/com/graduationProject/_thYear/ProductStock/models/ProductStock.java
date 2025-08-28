package com.graduationProject._thYear.ProductStock.models;

import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Unit.models.UnitItem;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.Builder.Default;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_stock", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_id", "warehouse_id","unit_item_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStock {

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_item_id", nullable = false)
    private UnitItem unitItem;

    @NotNull
    @Column(name = "quantity", nullable = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Must be positive")
    private BigDecimal quantity;
}
