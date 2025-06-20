package com.graduationProject._thYear.Journal.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.graduationProject._thYear.Journal.dtos.response.LedgerReport.LedgerEntry;
import com.graduationProject._thYear.Journal.models.JournalItem;

public interface JournalItemRepository extends JpaRepository<JournalItem, Integer> {

        @Query("SELECT ji FROM JournalItem ji WHERE ji.account.id = :accountId " +
                "AND COALESCE(ji.date, ji.jornalHeader.date) BETWEEN :startDate AND :endDate " +
                "ORDER BY ji.date, ji.id")
        List<JournalItem> findEntriesByAccountAndDateRange(
                @Param("accountId") Integer accountId,
                @Param("startDate") LocalDateTime startDate,
                @Param("endDate") LocalDateTime endDate);

        @Query("SELECT COALESCE(SUM(ji.debit), 0) FROM JournalItem ji " +
                "WHERE ji.account.id = :accountId AND COALESCE(ji.date, ji.jornalHeader.date) BETWEEN :startDate AND :endDate")
        BigDecimal calculateDebitBetweenDates(
                @Param("accountId") Integer accountId,
                @Param("startDate") LocalDateTime startDate,
                @Param("endDate") LocalDateTime endDate);

        // حساب إجمالي الدائن قبل تاريخ معين
        @Query("SELECT COALESCE(SUM(ji.credit), 0) FROM JournalItem ji " +
                "WHERE ji.account.id = :accountId AND COALESCE(ji.date, ji.jornalHeader.date) BETWEEN :startDate AND :endDate")
        BigDecimal calculateCreditBetweenDates(
                @Param("accountId") Integer accountId,
                @Param("startDate") LocalDateTime startDate,
                @Param("endDate") LocalDateTime endDate);

        @Query("SELECT COALESCE(SUM(ji.debit), 0) FROM JournalItem ji " +
                "WHERE ji.account.id = :accountId AND COALESCE(ji.date, ji.jornalHeader.date) < :date")
        BigDecimal calculateDebitBeforeDate(
                @Param("accountId") Integer accountId,
                @Param("date") LocalDateTime date);

        @Query("SELECT COALESCE(SUM(ji.credit), 0) FROM JournalItem ji " +
                "WHERE ji.account.id = :accountId AND COALESCE(ji.date, ji.jornalHeader.date) < :date")
        BigDecimal calculateCreditBeforeDate(
                @Param("accountId") Integer accountId,
                @Param("date") LocalDateTime date);
    }

