package com.graduationProject._thYear.Currency.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Entity
@Table(name = "currency")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


     @Column(name = "globalId", nullable = false, updatable = false)
    @Default
    private UUID globalId = UUID.randomUUID();

      @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

      @Column(name = "deletedAt")
    private LocalDateTime deletedAt;
    
    @NotNull
    @Column(name = "code" , unique = true, nullable = false)
    private String code;

    @NotNull
    @Column(name = "name" , unique = true ,nullable = false)
    private String name;

    @NotNull
    @Column(name = "currency_value" , nullable = false)
    private Float currencyValue;


    @Column(name = "part_name" )
    private String partName;

    @Column(name = "part_precision")
    private Integer partPrecision;

    @Column(name = "created_at")
    @Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
