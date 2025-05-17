package com.graduationProject._thYear.serviceImplmentation.currency;

import com.graduationProject._thYear.dto.request.currency.CreateCurrencyRequest;
import com.graduationProject._thYear.dto.request.currency.UpdateCurrencyRequest;
import com.graduationProject._thYear.dto.response.currency.CurrencyResponse;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import com.graduationProject._thYear.repository.CurrencyRepository;
import com.graduationProject._thYear.service.currency.CurrencyService;
import com.graduationProject._thYear.model.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Override
    public CurrencyResponse createCurrency(CreateCurrencyRequest request) {
        Currency currency = new Currency();
        currency.setCode(request.getCode());
        currency.setName(request.getName());
        currency.setCurrencyValue(request.getCurrencyValue());
        currency.setPartName(request.getPartName());
        currency.setPartPrecision(request.getPartPrecision());

        Currency savedCurrency = currencyRepository.save(currency);
        return convertToResponse(savedCurrency);
    }

    @Override
    public CurrencyResponse getCurrencyById(Integer id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + id));
        return convertToResponse(currency);
    }

    @Override
    public List<CurrencyResponse> getAllCurrencies() {
        return currencyRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CurrencyResponse updateCurrency(Integer id, UpdateCurrencyRequest request) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + id));

        currency.setCode(request.getCode());
        currency.setName(request.getName());
        currency.setCurrencyValue(request.getCurrencyValue());
        currency.setPartName(request.getPartName());
        currency.setPartPrecision(request.getPartPrecision());

        Currency updatedCurrency = currencyRepository.save(currency);
        return convertToResponse(updatedCurrency);
    }

    @Override
    public void deleteCurrency(Integer id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + id));
        currencyRepository.delete(currency);
    }

    private CurrencyResponse convertToResponse(Currency currency) {
        return CurrencyResponse.builder()
                .id(currency.getId())
                .code(currency.getCode())
                .name(currency.getName())
                .currencyValue(currency.getCurrencyValue())
                .partName(currency.getPartName())
                .partPrecision(currency.getPartPrecision())
                .build();
    }
}
