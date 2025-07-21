package com.graduationProject._thYear.Currency.seeders;

import com.graduationProject._thYear.Currency.dtos.requests.CreateCurrencyRequest;
import com.graduationProject._thYear.Currency.services.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencySeeder implements CommandLineRunner {

    private final CurrencyService currencyService;

    @Override
    public void run(String... args) {
//        CreateCurrencyRequest SP = CreateCurrencyRequest.builder()
//                .code("CU01")
//                .name("syria Bound")
//                .currencyValue(1.0f)
//                .partName("s.p")
//                .partPrecision(1)
//                .build();
//
//        CreateCurrencyRequest usd = CreateCurrencyRequest.builder()
//                .code("CU02")
//                .name("US Dollar")
//                .currencyValue(10000.0f)
//                .partName("$")
//                .partPrecision(1)
//                .build();
//
//        currencyService.createCurrency(SP);
//        currencyService.createCurrency(usd);
    }
}
