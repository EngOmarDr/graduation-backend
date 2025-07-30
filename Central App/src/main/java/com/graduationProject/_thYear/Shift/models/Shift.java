package com.graduationProject._thYear.Shift.models;

import com.graduationProject._thYear.Auth.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shift")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    
    @NotNull
    @Default
    @Column(name = "startDate",nullable = false)
    private LocalDateTime startDate = LocalDateTime.now();

    @Column(name = "endDate",nullable = true)
    private LocalDateTime endDate;

    @NotNull
    @Column(name = "startCash",nullable = false)
    private BigDecimal startCash;

    @Column(name = "endCash",nullable = true)
    private BigDecimal endCash;

    @Column(name = "expectedEndCash",nullable = true)
    private BigDecimal expectedEndCash;

    
    @Column(name = "notes", nullable = true)
    private String notes;

}
