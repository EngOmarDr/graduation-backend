package com.graduationProject._thYear.Journal.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.graduationProject._thYear.Journal.models.JournalHeader;

public interface JournalHeaderRepository extends JpaRepository<JournalHeader, Integer> {

  //  List<JournalHeader> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    @EntityGraph(attributePaths = {"journalItems", "branch", "currency"})
    List<JournalHeader> findAll();

    @EntityGraph(attributePaths = {"journalItems", "branch", "currency"})
    List<JournalHeader> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @EntityGraph(attributePaths = {"journalItems", "branch", "currency"})
    List<JournalHeader> findByParentType(Byte parentType);


}
