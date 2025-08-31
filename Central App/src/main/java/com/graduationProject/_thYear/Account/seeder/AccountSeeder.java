package com.graduationProject._thYear.Account.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Account.repositories.AccountRepository;

@Component
@Profile("!pos-app")
public class AccountSeeder implements CommandLineRunner {

    private final AccountRepository accountRepository;

    public AccountSeeder(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (accountRepository.existsByCode("01")) {
            return;
        }

        Account entity1 = new Account();
        entity1.setName("الميزانية");
        entity1.setCode("01");

        Account entity2 = new Account();
        entity2.setName("الأرباح و الخسائر");
        entity2.setCode("02");
        entity2.setParent(entity1);

        Account entity3 = new Account();
        entity3.setName("المتاجرة");
        entity3.setCode("03");
        entity3.setParent(entity2);

        Account entity4 = new Account();
        entity4.setName("حساب صندوق");
        entity4.setCode("04");

        Account entity5 = new Account();
        entity5.setName("حساب مشتريات");
        entity5.setCode("05");

        accountRepository.save(entity1);
        accountRepository.save(entity2);
        accountRepository.save(entity3);
        accountRepository.save(entity4);
        accountRepository.save(entity5);

    }
}