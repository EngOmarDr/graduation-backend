package com.graduationProject._thYear.service;

import com.graduationProject._thYear.dto.request.currency.CreateCurrencyRequest;
import com.graduationProject._thYear.dto.request.currency.UpdateCurrencyRequest;
import com.graduationProject._thYear.dto.response.currency.CurrencyResponse;

import java.util.List;

public interface CurrencyService {
    CurrencyResponse createCurrency(CreateCurrencyRequest request);
    CurrencyResponse getCurrencyById(Integer id);
    List<CurrencyResponse> getAllCurrencies();
    CurrencyResponse updateCurrency(Integer id, UpdateCurrencyRequest request);
    void deleteCurrency(Integer id);
}
