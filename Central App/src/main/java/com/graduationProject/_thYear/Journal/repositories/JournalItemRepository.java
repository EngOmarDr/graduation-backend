package com.graduationProject._thYear.Journal.repositories;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import com.graduationProject._thYear.Journal.models.JournalHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.graduationProject._thYear.Journal.models.JournalItem;

import jakarta.persistence.Tuple;
import com.graduationProject._thYear.Account.models.Account;


public interface JournalItemRepository extends JpaRepository<JournalItem, Integer> {

        @Query("SELECT ji FROM JournalItem ji WHERE ji.account.id = :accountId " +
                "AND (ji.date BETWEEN :startDate AND :endDate OR ji.journalHeader.date BETWEEN :startDate AND :endDate) " +
                "ORDER BY COALESCE(ji.date, ji.journalHeader.date), ji.id")
        List<JournalItem> findEntriesByAccountAndDateRange(
                @Param("accountId") Integer accountId,
                @Param("startDate") LocalDateTime startDate,
                @Param("endDate") LocalDateTime endDate);

        List<JournalItem> findByAccount(Account account);

        @Query("SELECT COALESCE(SUM(ji.debit), 0) FROM JournalItem ji " +
                "WHERE ji.account.id = :accountId AND (ji.date BETWEEN :startDate AND :endDate OR ji.journalHeader.date BETWEEN :startDate AND :endDate)")
        BigDecimal calculateDebitBetweenDates(
                @Param("accountId") Integer accountId,
                @Param("startDate") LocalDateTime startDate,
                @Param("endDate") LocalDateTime endDate);

        @Query("SELECT COALESCE(SUM(ji.credit), 0) FROM JournalItem ji " +
                "WHERE ji.account.id = :accountId AND (ji.date BETWEEN :startDate AND :endDate OR ji.journalHeader.date BETWEEN :startDate AND :endDate)")
        BigDecimal calculateCreditBetweenDates(
                @Param("accountId") Integer accountId,
                @Param("startDate") LocalDateTime startDate,
                @Param("endDate") LocalDateTime endDate);

        @Query("SELECT COALESCE(SUM(ji.debit), 0) FROM JournalItem ji " +
                "WHERE ji.account.id = :accountId AND (ji.date < :date OR ji.journalHeader.date < :date)")
        BigDecimal calculateDebitBeforeDate(
                @Param("accountId") Integer accountId,
                @Param("date") LocalDateTime date);

        @Query("SELECT COALESCE(SUM(ji.credit), 0) FROM JournalItem ji " +
                "WHERE ji.account.id = :accountId AND (ji.date < :date OR ji.journalHeader.date < :date)")
        BigDecimal calculateCreditBeforeDate(
                @Param("accountId") Integer accountId,
                @Param("date") LocalDateTime date);

        @Query("SELECT DISTINCT FUNCTION('DATE',ji.date) AS date, COALESCE(SUM(ji.debit),0) AS total_debit, COALESCE(SUM(ji.credit),0) AS total_credit FROM JournalItem ji "+
                "WHERE ji.date BETWEEN :startDate AND :endDate " +
                "GROUP BY FUNCTION('DATE',ji.date)")
        List<Tuple> getTotalDebitAndCreditByAccountWithinTimeRange(
                @Param("startDate") LocalDateTime startDate,
                @Param("endDate") LocalDateTime endDate);
        
        @Query("SELECT ji FROM JournalItem ji "+
                "WHERE FUNCTION('DATE',ji.date) = :date")
        List<JournalItem> getJournalItemsByDate(
             @Param("date") Date date);

         @Query("SELECT CASE WHEN COALESCE(SUM(ji.debit),0) > COALESCE(SUM(ji.credit),0) THEN COALESCE(SUM(ji.debit),0) - COALESCE(SUM(ji.credit),0) ELSE 0 END  AS total_debit, " +
                "CASE WHEN COALESCE(SUM(ji.credit),0) > COALESCE(SUM(ji.debit),0) THEN COALESCE(SUM(ji.credit),0) - COALESCE(SUM(ji.debit),0) ELSE 0 END AS total_credit " + 
                "FROM JournalItem ji "+
                "WHERE ji.date BETWEEN :startDate AND :endDate ")
        Tuple getTotalDebitAndCreditWithinTimeRange(
                @Param("startDate") LocalDateTime startDate,
                @Param("endDate") LocalDateTime endDate);
        
        @Query("SELECT  a.id AS accountId, a.name AS accountName, a.code AS accountCode, " +
                "CASE WHEN COALESCE(SUM(ji.debit),0) > COALESCE(SUM(ji.credit),0) THEN COALESCE(SUM(ji.debit),0) - COALESCE(SUM(ji.credit),0) ELSE 0 END  AS total_debit, " +
                "CASE WHEN COALESCE(SUM(ji.credit),0) > COALESCE(SUM(ji.debit),0) THEN COALESCE(SUM(ji.credit),0) - COALESCE(SUM(ji.debit),0) ELSE 0 END AS total_credit " + 
                "FROM JournalItem ji "+
                "JOIN ji.account a " +

                "WHERE ji.date BETWEEN :startDate AND :endDate " +
                "GROUP BY ji.account"
                )
        List<Tuple> getTotalDebitAndCreditByAccount(
                @Param("startDate") LocalDateTime startDate,
                @Param("endDate") LocalDateTime endDate);
        
        
        @Modifying
        @Query("DELETE FROM JournalItem ji WHERE ji.journalHeader = :journalHeader")
        void deleteAllByJournalHeader(@Param("journalHeader") JournalHeader journalHeader);
}