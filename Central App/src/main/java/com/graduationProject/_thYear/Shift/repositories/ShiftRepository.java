package com.graduationProject._thYear.Shift.repositories;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.graduationProject._thYear.Shift.models.Shift;

public interface ShiftRepository extends JpaRepository<Shift,Integer> {

    

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
}


