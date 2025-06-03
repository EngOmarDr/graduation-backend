package com.graduationProject._thYear.Journal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.graduationProject._thYear.Journal.models.JournalHeader;

public interface JournalHeaderRepository extends JpaRepository<JournalHeader, Integer> {
}
