package com.graduationProject._thYear.Warehouse.models;

import com.graduationProject._thYear.Branch.models.Branch;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "warehouse")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Warehouse {
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


    @NotNull
    @Column(name = "code" , unique = true, nullable = false)
    private String code;

    @Column(name = "phone")
    private String phone;


    @Column(name = "address" )
    private String address;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Warehouse parent;

    @OneToMany(mappedBy = "parent")
    private List<Warehouse> children ;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WarehouseType type; // WAREHOUSE , POS

    private boolean isActive ;

    @Column(name = "notes")
    private String notes;

}
