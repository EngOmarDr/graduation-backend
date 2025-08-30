package com.graduationProject._thYear.Account.repositories;

import com.graduationProject._thYear.Account.models.Account;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByCode(String code);

    boolean existsByName(String name);

    List<Account> findByParentIsNull(); // For getting root categories

    List<Account> findByParentId(Integer parentId); // For getting children of a specific category

    Optional<Account> findByGlobalId(UUID globalId);


    @Query("""
        SELECT a FROM Account a 
        WHERE a.finalAccount = null
        """)
    List<Account> findFinalAccounts();

    @Query("SELECT a FROM Account a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(a.code) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Account> searchByNameOrCode(@Param("searchTerm") String searchTerm);

      
     @Query(value = """
                SELECT a FROM Account a
                WHERE :date IS null OR a.createdAt > :date OR a.updatedAt > :date
           """)
     Slice<Account> findAllByUpsertedAtAfter(LocalDateTime date, PageRequest pageRequest);

   

     @Query(value = """
                SELECT a FROM Account a
                WHERE :date IS null OR a.deletedAt > :date
           """)
     Slice<Account> findAllByDeletetedAtAfter(LocalDateTime date);

}
