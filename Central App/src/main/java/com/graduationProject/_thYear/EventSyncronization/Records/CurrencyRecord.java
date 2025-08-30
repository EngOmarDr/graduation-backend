package com.graduationProject._thYear.EventSyncronization.Records;

import java.time.LocalDateTime;
import java.util.UUID;

import com.graduationProject._thYear.Currency.models.Currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrencyRecord {
    private UUID globalId;
    private String code;
    private String name;
    private Float currencyValue;
    private String partName;
    private Integer partPrecision;
    private LocalDateTime createdAt;
    @Default
    private Boolean isDeleted = false;

    public static CurrencyRecord fromCurrencyEntity(Currency currency){
        return CurrencyRecord.builder()
            .globalId(currency.getGlobalId())
            .code(currency.getCode())
            .name(currency.getName())
            .partPrecision(currency.getPartPrecision())
            .currencyValue(currency.getCurrencyValue())
            .build();
    }
}
