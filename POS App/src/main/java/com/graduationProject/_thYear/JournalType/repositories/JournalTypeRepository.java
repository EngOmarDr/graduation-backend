package com.graduationProject._thYear.JournalType.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.graduationProject._thYear.JournalType.models.JournalType;

public interface JournalTypeRepository extends JpaRepository<JournalType, Short> {
    boolean existsByName(String name);
}
