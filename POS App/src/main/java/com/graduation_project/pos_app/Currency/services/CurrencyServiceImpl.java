package com.graduation_project.pos_app.Currency.services;

import com.graduation_project.pos_app.Currency.dtos.requests.CreateCurrencyRequest;
import com.graduation_project.pos_app.Currency.dtos.requests.UpdateCurrencyRequest;
import com.graduation_project.pos_app.Currency.dtos.responses.CurrencyResponse;
import com.graduation_project.pos_app.exceptionHandler.ResourceNotFoundException;
import com.graduation_project.pos_app.Currency.repositories.CurrencyRepository;
import com.graduation_project.pos_app.Currency.models.Currency;
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

        // Validate name and code uniqueness
        if (currencyRepository.existsByCode(request.getName())) {
            throw new IllegalArgumentException("Currency item with code '" + request.getCode() + "' already exists");
        }
        if (currencyRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Currency item with name '" + request.getName() + "' already exists");
        }

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

        if (!currency.getCode().equals(request.getCode()) &&
                currencyRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Currency item with code '" + request.getCode() + "' already exists");
        }

        if (!currency.getName().equals(request.getName()) &&
                currencyRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Currency item with name '" + request.getName() + "' already exists");
        }

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
