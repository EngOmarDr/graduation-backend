package com.graduation_project.pos_app.Currency.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
