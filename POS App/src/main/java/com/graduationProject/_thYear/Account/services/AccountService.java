package com.graduationProject._thYear.Account.services;

import com.graduationProject._thYear.Account.dtos.request.CreateAccountRequest;
import com.graduationProject._thYear.Account.dtos.request.UpdateAccountRequest;
import com.graduationProject._thYear.Account.dtos.response.AccountResponse;
import com.graduationProject._thYear.Account.dtos.response.AccountTreeResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(CreateAccountRequest request);

    AccountResponse getAccountById(Integer id);

    List<AccountResponse> getAllAccounts();

    List<AccountTreeResponse> getAccountsTree();

    List<AccountResponse> getChildAccounts(Integer parentId);

    AccountResponse updateAccount(Integer id, UpdateAccountRequest request);

    void deleteAccount(Integer id);

    List<AccountResponse> searchAccounts(String searchTerm);
}
