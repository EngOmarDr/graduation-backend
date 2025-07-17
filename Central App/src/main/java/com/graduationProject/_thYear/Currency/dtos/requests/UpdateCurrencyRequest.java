package com.graduationProject._thYear.Currency.dtos.requests;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCurrencyRequest {
    private String code;

    private String name;

    @DecimalMin(value = "0.00", inclusive = true, message = "currencyValue must be positive")
    private Float currencyValue;

    private String partName;
    private Integer partPrecision;
}