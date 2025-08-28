package com.graduationProject._thYear.Transfer.models;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "transfer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
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
    @JoinColumn(name = "fromWarehouseId", nullable = false)
    private Warehouse fromWarehouseId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toWarehouseId", nullable = false)
    private Warehouse toWarehouseId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cashAccountId", nullable = false)
    private Account cashAccountId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expenseAccountId", nullable = false)
    private Account expenseAccountId;

    @DecimalMin(value = "0.00", inclusive = true ,message = "Value must be positive")
    @Column(name = "expenseValue")
    private BigDecimal expenseValue;

    @NotNull
    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "driverName")
    private String driverName;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransferItem> items = new ArrayList<>();

    public void addItem(TransferItem item) {
        items.add(item);
        item.setTransfer(this);
    }
}
