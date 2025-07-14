package com.graduationProject._thYear.Account.services;

import com.graduationProject._thYear.Account.dtos.request.CreateAccountRequest;
import com.graduationProject._thYear.Account.dtos.request.UpdateAccountRequest;
import com.graduationProject._thYear.Account.dtos.response.AccountResponse;
import com.graduationProject._thYear.Account.dtos.response.AccountTreeResponse;
import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Account.repositories.AccountRepository;
import com.graduationProject._thYear.Journal.repositories.JournalItemRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final JournalItemRepository journalItemRepository;

    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        if (accountRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Account with code '" + request.getCode() + "' already exists");
        }
        if (accountRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Account with name '" + request.getName() + "' already exists");
        }
        // Todo: review these condition
        if (request.getFinalAccount() == null) {
            throw new IllegalArgumentException("final Account must be not null");
        }
        if (!List.of(1, 2, 3).contains(request.getFinalAccount())) {
            throw new IllegalArgumentException("final Account must be الميزانية or الأرباح و الخسائر or المتاجرة");
        }

        var finalAccount = accountRepository.findById(request.getFinalAccount())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "final account not found with id: " + request.getFinalAccount()));

        Account parentAccount = null;
        if (request.getParentId() != null) {
            parentAccount = accountRepository.findById(request.getParentId()).orElse(null);
        }

        Account account = Account.builder()
                .code(request.getCode())
                .name(request.getName())
                .parent(parentAccount)
                .finalAccount(finalAccount)
                .build();

        var savedAccount = accountRepository.save(account);
        return convertToResponse(savedAccount);
    }

    @Override
    public AccountResponse getAccountById(Integer id) {
        var res = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        return convertToResponse(res);
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountTreeResponse> getAccountsTree() {
        var rootGroups = accountRepository.findByParentIsNull();
        return rootGroups.stream()
                .map(this::convertToTreeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponse> getChildAccounts(Integer parentId) {
        return accountRepository.findByParentId(parentId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponse updateAccount(Integer id, UpdateAccountRequest request) {
        var res = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        // Validate unique code and name if changed
        if (!res.getCode().equals(request.getCode()) &&
                accountRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Account with code '" + request.getCode() + "' already exists");
        }
        if (!res.getName().equals(request.getName()) &&
                accountRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Account with name '" + request.getName() + "' already exists");
        }
        if (request.getFinalAccount() == null) {
            throw new IllegalArgumentException("final Account must be not null");
        }
        var finalAccount = accountRepository.findById(request.getFinalAccount())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "final account not found with id: " + request.getFinalAccount()));
        var parentAccount = accountRepository.findById(request.getParentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "parent account not found with id: " + request.getFinalAccount()));

        res.setCode(request.getCode());
        res.setName(request.getName());
        res.setFinalAccount(finalAccount);
        res.setParent(parentAccount);

        var updatedRes = accountRepository.save(res);
        return convertToResponse(updatedRes);
    }

    @Override
    public void deleteAccount(Integer id) {
        var res = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        if (res.getFinalAccount() == null){
            throw new RuntimeException("Final accounts could not be deleted");
        }        

        if (journalItemRepository.findByAccount(res).size() > 0){
            throw new RuntimeException("Accounts with recorded journals can't be deleted");
        }
        accountRepository.delete(res);
    }

    public List<AccountResponse> searchAccounts(String searchTerm) {
        return accountRepository.searchByNameOrCode(searchTerm).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private AccountResponse convertToResponse(Account account) {
        Integer finalAccountId = null;
        if (account.getFinalAccount() != null && account.getFinalAccount().getId() != null) {
            finalAccountId = account.getFinalAccount().getId();
        }
        Integer parentId = null;
        if (account.getParent() != null && account.getParent().getId() != null) {
            parentId = account.getParent().getId();
        }
        return AccountResponse.builder()
                .id(account.getId())
                .code(account.getCode())
                .name(account.getName())
                .finalAccount(finalAccountId)
                .parentId(parentId)
                .build();
    }

    private AccountTreeResponse convertToTreeResponse(Account account) {
        String finalAccountName = null;
        if (account.getFinalAccount() != null)
            finalAccountName = account.getFinalAccount().getName();

        return AccountTreeResponse.builder()
                .id(account.getId())
                .code(account.getCode())
                .name(account.getName())
                .finalAccountName(finalAccountName)
                .children(account.getChildren().stream()
                        .map(this::convertToTreeResponse)
                        .collect(Collectors.toList()))
                .build();
    }

}
