package com.graduationProject._thYear.Currency.dtos.requests;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCurrencyRequest {
    @NotNull(message = "Code is required")
    private String code;

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Currency value is required")
    private Float currencyValue;

    private String partName;
    private Integer partPrecision;
}