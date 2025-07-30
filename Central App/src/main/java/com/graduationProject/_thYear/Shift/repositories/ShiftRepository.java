package com.graduationProject._thYear.Shift.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.graduationProject._thYear.Shift.models.Shift;

public interface ShiftRepository extends JpaRepository<Shift,Integer> {

    @Query(
        "SELECT shift From Shift shift " +
        "WHERE (:startDate IS NULL OR shift.startDate > :startDate) " +
        "AND (:endDate IS NULL OR shift.startDate < :endDate) " +
        "AND (:userId IS NULL OR shift.user.id = :userId) " +
        "AND (:isClosed IS NULL OR :isClosed = true OR shift.endDate IS NULL) " +
        "AND (:isClosed IS NULL OR :isClosed = false OR shift.endDate IS NOT NULL) "
    )
    List<Shift> listShifts(Integer userId, LocalDateTime startDate, LocalDateTime endDate, Boolean isClosed, Sort sort);


    @Query(
        "SELECT COALESCE(SUM(inv.total),0) From Shift shift " +
        "JOIN InvoiceHeader inv ON inv.user.id = shift.user.id " +
        "WHERE inv.date > shift.startDate"
    )
    BigDecimal getCurrentCash(Integer shiftId);

    @Query(
        "SELECT COALESCE(SUM(inv.total),0) From Shift shift " +
        "JOIN InvoiceHeader inv ON inv.user.id = shift.user.id " +
        "WHERE inv.date > shift.startDate " +
        "AND inv.date < shift.endDate " 
    )
    BigDecimal getEndCash(Integer shiftId);


    List<Shift> findByUserId(Integer userId, Sort sort);
}


