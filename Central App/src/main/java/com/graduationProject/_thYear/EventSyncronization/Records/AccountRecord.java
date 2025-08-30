package com.graduationProject._thYear.EventSyncronization.Records;

import java.util.UUID;

import com.graduationProject._thYear.Account.models.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRecord {
    private UUID globalId;
    private String code;
    private String name;
    private AccountRecord parent;
    private AccountRecord finalAccount;
    @Default
    private Boolean isDeleted = false;

    public static AccountRecord fromAccountEntity(Account account){
        if (account == null){
            return null;
        }
        return AccountRecord.builder()
            .globalId(account.getGlobalId())
            .code(account.getCode())
            .name(account.getName())
            .parent(fromAccountEntity(account.getParent()))
            .finalAccount(fromAccountEntity(account.getFinalAccount()))
            .build();
    }
}
