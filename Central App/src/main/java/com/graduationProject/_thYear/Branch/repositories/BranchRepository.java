package com.graduationProject._thYear.Branch.repositories;

import com.graduationProject._thYear.Branch.models.Branch;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Integer> {
    boolean existsByPhone(String phone);

    boolean existsByName(String name);

    Optional<Branch> findByGlobalId(UUID globalId);
}
