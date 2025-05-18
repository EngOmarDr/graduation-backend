package com.graduationProject._thYear.Currency.services;

import com.graduationProject._thYear.Currency.dtos.requests.CreateCurrencyRequest;
import com.graduationProject._thYear.Currency.dtos.requests.UpdateCurrencyRequest;
import com.graduationProject._thYear.Currency.dtos.responses.CurrencyResponse;

import java.util.List;

public interface CurrencyService {
    CurrencyResponse createCurrency(CreateCurrencyRequest request);
    CurrencyResponse getCurrencyById(Integer id);
    List<CurrencyResponse> getAllCurrencies();
    CurrencyResponse updateCurrency(Integer id, UpdateCurrencyRequest request);
    void deleteCurrency(Integer id);
}
