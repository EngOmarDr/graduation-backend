package com.graduationProject._thYear.EventSyncronization.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.graduationProject._thYear.EventSyncronization.Entities.SyncJob;

public interface SyncJobRepository extends JpaRepository<SyncJob, Integer> {

    @Query(
        value = """
            SELECT sj FROM SyncJob sj 
            WHERE topic = :topic
            ORDER BY sj.executedAt DESC
            LIMIT 1
        """
    )
    SyncJob findLastByTopic(String topic);
}
