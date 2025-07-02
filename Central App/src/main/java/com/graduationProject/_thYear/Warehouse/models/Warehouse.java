package com.graduationProject._thYear.Warehouse.models;

import com.graduationProject._thYear.Branch.models.Branch;
import com.graduationProject._thYear.Group.models.Group;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "warehouse")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "name" , unique = true , nullable = false)
    private String name;


    @NotNull
    @Column(name = "code" , unique = true, nullable = false)
    private String code;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @NotNull
    @Column(name = "address" , nullable = false)
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

    private boolean isActive = true ;

    @Column(name = "notes")
    private String notes;

}
