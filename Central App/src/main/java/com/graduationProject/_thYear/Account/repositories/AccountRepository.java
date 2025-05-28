package com.graduationProject._thYear.Account.repositories;

import com.graduationProject._thYear.Account.models.Account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByCode(String code);

    boolean existsByName(String name);

    List<Account> findByParentIsNull(); // For getting root categories

    List<Account> findByParentId(Integer parentId); // For getting children of a specific category
}
