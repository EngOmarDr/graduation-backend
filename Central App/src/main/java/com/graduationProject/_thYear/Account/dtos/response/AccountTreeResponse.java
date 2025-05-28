package com.graduationProject._thYear.Account.dtos.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

import com.graduationProject._thYear.Account.models.Account;

@Setter
@Getter
@SuperBuilder
public class AccountTreeResponse {
    private Integer id;
    private String code;
    private String name;
    private Account finalAccount;
    private List<AccountTreeResponse> children;
}
