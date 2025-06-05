package com.graduationProject._thYear.JournalType.services;

import com.graduationProject._thYear.Account.dtos.response.AccountResponse;
import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Account.repositories.AccountRepository;
import com.graduationProject._thYear.Currency.dtos.responses.CurrencyResponse;
import com.graduationProject._thYear.Currency.models.Currency;
import com.graduationProject._thYear.Currency.repositories.CurrencyRepository;
import com.graduationProject._thYear.JournalType.dtos.request.CreateJournalTypeRequest;
import com.graduationProject._thYear.JournalType.dtos.request.UpdateJournalTypeRequest;
import com.graduationProject._thYear.JournalType.dtos.response.JournalTypeResponse;
import com.graduationProject._thYear.JournalType.models.JournalType;
import com.graduationProject._thYear.JournalType.repositories.JournalTypeRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class JournalTypeServiceImpl implements JournalTypeService {
    private final JournalTypeRepository repository;
    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;

    private AccountResponse convertAcccountToResponse(Account account) {
        Integer finalAccountId = null;
        if (account.getFinalAccount() != null && account.getFinalAccount().getId() != null) {
            finalAccountId = account.getFinalAccount().getId();
        }
        return AccountResponse.builder()
                .id(account.getId())
                .code(account.getCode())
                .name(account.getName())
                .finalAccount(finalAccountId)
                .build();
    }

    private CurrencyResponse convertCurrencyToResponse(Currency currency) {
        return CurrencyResponse.builder()
                .id(currency.getId())
                .code(currency.getCode())
                .name(currency.getName())
                .currencyValue(currency.getCurrencyValue())
                .partName(currency.getPartName())
                .partPrecision(currency.getPartPrecision())
                .build();
    }

    private JournalTypeResponse convertToResponse(JournalType journalType) {
        return JournalTypeResponse.builder()
                .id(journalType.getId())
                .name(journalType.getName())
                .autoPost(journalType.getAutoPost())
                .creditName(journalType.getCreditName())
                .debitName(journalType.getDebitName())
                .defaultCurrency(journalType.getDefaultCurrency() == null ? null
                        : convertCurrencyToResponse(journalType.getDefaultCurrency()))
                .defaultAccountId(journalType.getDefaultAccount() == null ? null
                        : convertAcccountToResponse(journalType.getDefaultAccount()))
                .fieldCredit(journalType.getFieldCredit())
                .fieldCurrencyEquilty(journalType.getFieldCurrencyEquilty())
                .fieldCurrencyName(journalType.getFieldCurrencyName())
                .fieldDate(journalType.getFieldDate())
                .fieldDebit(journalType.getFieldDebit())
                .fieldNotes(journalType.getFieldNotes())
                .numberFormat(journalType.getNumberFormat())
                .build();
    }

    @Override
    public JournalTypeResponse createJournalType(CreateJournalTypeRequest request) {
        if (repository.existsByName(request.getName())) {
            throw new IllegalArgumentException("journal type with name '" + request.getName() + "' already exists");
        }
        Account account = null;
        if (request.getDefaultAccountId() != null) {
            account = accountRepository.findById(request.getDefaultAccountId()).orElseThrow(
                    () -> new IllegalArgumentException(
                            "Account with id '" + request.getDefaultAccountId() + "' not exists"));
        }
        Currency currency = null;
        if (request.getDefaultCurrencyId() != null) {
            currency = currencyRepository.findById(request.getDefaultCurrencyId()).orElseThrow(
                    () -> new IllegalArgumentException(
                            "currency with id '" + request.getDefaultCurrencyId() + "' not exists"));
        }

        var journalType = new JournalType();
        journalType.setName(request.getName());
        journalType.setAutoPost(request.getAutoPost());
        journalType.setCreditName(request.getCreditName());
        journalType.setDebitName(request.getDebitName());
        journalType.setDefaultCurrency(currency);
        journalType.setDefaultAccount(account);
        journalType.setFieldCredit(request.getFieldCredit());
        journalType.setFieldCurrencyEquilty(request.getFieldCurrencyEquilty());
        journalType.setFieldCurrencyName(request.getFieldCurrencyName());
        journalType.setFieldDate(request.getFieldDate());
        journalType.setFieldDebit(request.getFieldDebit());
        journalType.setFieldNotes(request.getFieldNotes());
        journalType.setNumberFormat(request.getNumberFormat());

        var savedObject = repository.save(journalType);
        return convertToResponse(savedObject);
    }

    @Override
    public JournalTypeResponse getJournalTypeById(Short id) {
        var object = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal Type not found"));
        return convertToResponse(object);
    }

    @Override
    public List<JournalTypeResponse> getAllJournalType() {
        return repository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public JournalTypeResponse updateJournalType(Short id, UpdateJournalTypeRequest request) {
        var object = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("journal type not found"));

        if (!object.getName().equals(request.getName()) &&
                repository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Journal Type with name '" + request.getName() + "' already exists");
        }

        Account account = null;
        if (request.getDefaultAccountId() != null) {
            account = accountRepository.findById(request.getDefaultAccountId()).orElseThrow(
                    () -> new IllegalArgumentException(
                            "Account with id '" + request.getDefaultAccountId() + "' not exists"));
        }
        Currency currency = null;
        if (request.getDefaultCurrencyId() != null) {
            currency = currencyRepository.findById(request.getDefaultCurrencyId()).orElseThrow(
                    () -> new IllegalArgumentException(
                            "currency with id '" + request.getDefaultCurrencyId() + "' not exists"));
        }

        object.setName(request.getName());
        object.setAutoPost(request.getAutoPost());
        object.setCreditName(request.getCreditName());
        object.setDebitName(request.getDebitName());
        object.setDefaultCurrency(currency);
        object.setDefaultAccount(account);
        object.setFieldCredit(request.getFieldCredit());
        object.setFieldCurrencyEquilty(request.getFieldCurrencyEquilty());
        object.setFieldCurrencyName(request.getFieldCurrencyName());
        object.setFieldDate(request.getFieldDate());
        object.setFieldDebit(request.getFieldDebit());
        object.setFieldNotes(request.getFieldNotes());
        object.setNumberFormat(request.getNumberFormat());

        var updatedObject = repository.save(object);
        return convertToResponse(updatedObject);
    }

    @Override
    public void deleteJournalType(Short id) {
        JournalType object = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal Type not found"));

        repository.delete(object);
    }
}
