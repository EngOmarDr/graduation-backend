package com.graduationProject._thYear.Product.models;

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

    @OneToMany(mappedBy = "priceId", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductPrice> productPrices = new ArrayList<>();
}
