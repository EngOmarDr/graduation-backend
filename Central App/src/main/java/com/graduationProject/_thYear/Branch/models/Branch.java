package com.graduationProject._thYear.Branch.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "branch")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

        @Column(name = "globalId", nullable = false, updatable = false)
    @Default
    private UUID globalId = UUID.randomUUID();

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;
    
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "notes", nullable = true)
    private String notes;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
}
