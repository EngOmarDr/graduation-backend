package com.graduationProject._thYear.Account.dtos.response;


import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AccountResponse {
    private Integer id;
    private String code;
    private String name;
    private Integer parentId;
    private String parentName;
    private Integer finalAccount;
    private String finalAccountName;

}
