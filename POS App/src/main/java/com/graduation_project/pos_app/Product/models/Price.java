package com.graduation_project.pos_app.Product.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "price")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "name" , unique = true , nullable = false)
    private String name;

    @OneToMany(mappedBy = "priceId", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductPrice> productPrices = new ArrayList<>();
}
