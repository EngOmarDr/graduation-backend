package com.graduationProject._thYear.Currency.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class CurrencyResponse {
    private Integer id;
    private String code;
    private String name;
    private Float currencyValue;
    private String partName;
    private Integer partPrecision;
}
