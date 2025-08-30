package com.graduationProject._thYear.Currency.services;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Currency.dtos.requests.CreateCurrencyRequest;
import com.graduationProject._thYear.Currency.dtos.requests.UpdateCurrencyRequest;
import com.graduationProject._thYear.Currency.dtos.responses.CurrencyResponse;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import com.graduationProject._thYear.Currency.repositories.CurrencyRepository;
import com.graduationProject._thYear.EventSyncronization.Records.AccountRecord;
import com.graduationProject._thYear.EventSyncronization.Records.CurrencyRecord;
import com.graduationProject._thYear.Currency.models.Currency;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
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
        
        Currency defaultCurrency = currencyRepository.findAll(Sort.by("createdAt") ).get(0); 
        if (id == defaultCurrency.getId()){
            request.setCurrencyValue(defaultCurrency.getCurrencyValue());
        }

        for (Currency c: currencyRepository.findAll(Sort.by("createdAt") )){
            System.out.println(c.getCreatedAt());
            System.out.println(c.getName());
        }
        // Update code if provided and unique
        if (request.getCode() != null && !currency.getCode().equals(request.getCode())) {
            if (currencyRepository.existsByCode(request.getCode())) {
                throw new IllegalArgumentException("Currency item with code '" + request.getCode() + "' already exists");
            }
            currency.setCode(request.getCode());
        }

        // Update name if provided and unique
        if (request.getName() != null && !currency.getName().equals(request.getName())) {
            if (currencyRepository.existsByName(request.getName())) {
                throw new IllegalArgumentException("Currency item with name '" + request.getName() + "' already exists");
            }
            currency.setName(request.getName());
        }

        if (request.getCurrencyValue() != null) {
            currency.setCurrencyValue(request.getCurrencyValue());
        }

        if (request.getPartName() != null) {
            currency.setPartName(request.getPartName());
        }

        if (request.getPartPrecision() != null) {
            currency.setPartPrecision(request.getPartPrecision());
        }

        Currency updatedCurrency = currencyRepository.save(currency);
        return convertToResponse(updatedCurrency);
    }


    @Override
    public void deleteCurrency(Integer id) {
        Currency currency = currencyRepository.findById(id)
                .filter(c -> !c.getName().startsWith("syria Bound"))
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + id));
        currencyRepository.delete(currency);
    }


    @Override
    public Currency saveOrUpdate(CurrencyRecord currencyRecord){
        if (currencyRecord == null){
            return null;
        }
    
        Currency currency = currencyRepository.findByGlobalId(currencyRecord.getGlobalId())
            .orElse(new Currency());

        currency = currency.toBuilder()
            .globalId(currencyRecord.getGlobalId())
            .code(currencyRecord.getCode())
            .name(currencyRecord.getName())
            .currencyValue(currencyRecord.getCurrencyValue())
            .partPrecision(currencyRecord.getPartPrecision())
            .partName(currencyRecord.getPartName())
            .build();

        currencyRepository.save(currency);
        return currency;
    }
    private CurrencyResponse convertToResponse(Currency currency) {
        return CurrencyResponse.builder()
                .id(currency.getId())
                .code(currency.getCode())
                .name(currency.getName())
                .currencyValue(currency.getCurrencyValue())
                .partName(currency.getPartName())
                .partPrecision(currency.getPartPrecision())
                .createdAt(currency.getCreatedAt())
                .build();
    }
}
