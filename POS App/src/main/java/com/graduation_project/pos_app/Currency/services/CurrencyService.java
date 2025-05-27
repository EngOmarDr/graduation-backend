package com.graduation_project.pos_app.Currency.services;

import com.graduation_project.pos_app.Currency.dtos.requests.CreateCurrencyRequest;
import com.graduation_project.pos_app.Currency.dtos.requests.UpdateCurrencyRequest;
import com.graduation_project.pos_app.Currency.dtos.responses.CurrencyResponse;

import java.util.List;

public interface CurrencyService {
    CurrencyResponse createCurrency(CreateCurrencyRequest request);
    CurrencyResponse getCurrencyById(Integer id);
    List<CurrencyResponse> getAllCurrencies();
    CurrencyResponse updateCurrency(Integer id, UpdateCurrencyRequest request);
    void deleteCurrency(Integer id);
}
