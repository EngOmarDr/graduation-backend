package com.graduationProject._thYear.Journal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.graduationProject._thYear.Journal.models.JournalItem;

public interface JournalItemRepository extends JpaRepository<JournalItem, Integer> {
}
