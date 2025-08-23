package com.graduationProject._thYear.Auth.repositories;

import com.graduationProject._thYear.Auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

//    // Number of POS (cashiers)
//    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'CASHIER'")
//    Long getTotalPOS();
//
//    // Active employees (users who issued invoices this month)
//    @Query("SELECT COUNT(DISTINCT inv.user.id) " +
//            "FROM InvoiceHeader inv " +
//            "WHERE inv.date BETWEEN :startDate AND :endDate")
//    Long getActiveEmployees(LocalDateTime startDate, LocalDateTime endDate);
//

}
