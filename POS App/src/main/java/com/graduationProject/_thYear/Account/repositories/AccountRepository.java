package com.graduationProject._thYear.Account.repositories;

import com.graduationProject._thYear.Account.models.Account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByCode(String code);

    boolean existsByName(String name);

    List<Account> findByParentIsNull(); // For getting root categories

    List<Account> findByParentId(Integer parentId); // For getting children of a specific category

    @Query("""
        SELECT a FROM Account a 
        WHERE a.finalAccount = null
        """)
    List<Account> findFinalAccounts();

    @Query("SELECT a FROM Account a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(a.code) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Account> searchByNameOrCode(@Param("searchTerm") String searchTerm);
}
