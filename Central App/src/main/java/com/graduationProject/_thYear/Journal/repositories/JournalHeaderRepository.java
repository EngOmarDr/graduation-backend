package com.graduationProject._thYear.Journal.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.graduationProject._thYear.Journal.models.JournalKind;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.graduationProject._thYear.Journal.models.JournalHeader;

public interface JournalHeaderRepository extends JpaRepository<JournalHeader, Integer> {

  //  List<JournalHeader> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    @EntityGraph(attributePaths = {"journalItems", "branch", "currency"})
    List<JournalHeader> findAll();


    @EntityGraph(attributePaths = {"journalItems", "branch", "currency"})
    @Query(
      "SELECT jh FROM JournalHeader jh " +
      "WHERE (:branchId IS NULL OR jh.branch.id = :branchId) " +
      "AND (:parentType IS NULL OR jh.parentType = :parentType) " +
      "AND (:startDate IS NULL OR jh.date > :startDate) " +
      "AND (:endDate IS NULL OR jh.date < :endDate) " 
    )

    List<JournalHeader> list(Integer branchId, Byte parentType, LocalDateTime startDate, LocalDateTime endDate);

    @EntityGraph(attributePaths = {"journalItems", "branch", "currency"})
    List<JournalHeader> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @EntityGraph(attributePaths = {"journalItems", "branch", "currency"})
    List<JournalHeader> findByParentType(Byte parentType);

  Optional<JournalHeader> findByKindAndParentId(JournalKind kind, Integer parentId);


}
