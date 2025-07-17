package com.graduationProject._thYear.Transfer.models;

import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Unit.models.UnitItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "transferItem")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransferItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transferId", nullable = false)
    private Transfer transfer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    private Product productId;

    @NotNull
    @Column(name = "qty", nullable = false)
    @DecimalMin(value = "0.00", inclusive = true, message = "Value must be positive")
    private BigDecimal qty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unitItemId")
    private UnitItem unitItemId;

    @Column(name = "unitFact")
    @DecimalMin(value = "0.00", inclusive = true ,message = "Value must be positive")
    private BigDecimal unitFact;

}
