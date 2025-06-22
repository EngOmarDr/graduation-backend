package com.graduationProject._thYear.Journal.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.graduationProject._thYear.Journal.models.JournalHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.graduationProject._thYear.Journal.models.JournalItem;

public interface JournalItemRepository extends JpaRepository<JournalItem, Integer> {

        @Query("SELECT ji FROM JournalItem ji WHERE ji.account.id = :accountId " +
                "AND (ji.date BETWEEN :startDate AND :endDate OR ji.journalHeader.date BETWEEN :startDate AND :endDate) " +
                "ORDER BY COALESCE(ji.date, ji.journalHeader.date), ji.id")
        List<JournalItem> findEntriesByAccountAndDateRange(
                @Param("accountId") Integer accountId,
                @Param("startDate") LocalDateTime startDate,
                @Param("endDate") LocalDateTime endDate);

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

        @Modifying
        @Query("DELETE FROM JournalItem ji WHERE ji.journalHeader = :journalHeader")
        void deleteAllByJournalHeader(@Param("journalHeader") JournalHeader journalHeader);
}