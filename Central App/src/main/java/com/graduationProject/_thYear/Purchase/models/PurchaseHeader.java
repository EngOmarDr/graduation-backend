package com.graduationProject._thYear.Purchase.models;

import com.graduationProject._thYear.Transfer.models.TransferItem;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchaseHeader")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PurchaseHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WarehouseId", nullable = false)
    private Warehouse WarehouseId;


    @Column(name = "supplyDate")
    private LocalDateTime supplyDate;


    @Column(name = "requestDate")
    private LocalDateTime requestDate;


    @Column(name = "buyDate")
    private LocalDateTime buyDate;


    @Column(name = "receiveDate")
    private LocalDateTime receiveDate;

    @NotNull
    @Column(name = "status")
    private StatusType status; // supply - request - buy - receive

    @Column(name = "notes")
    private String notes;


    @OneToMany(mappedBy = "purchaseHeader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseItem> items = new ArrayList<>();

    public void addItem(PurchaseItem item) {
        items.add(item);
        item.setPurchaseHeader(this);
    }


}
