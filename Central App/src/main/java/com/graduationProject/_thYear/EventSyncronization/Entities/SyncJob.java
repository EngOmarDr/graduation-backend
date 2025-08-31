package com.graduationProject._thYear.EventSyncronization.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Entity
@Table(name = "sync_jobs")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SyncJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "topic")
    private String topic;

    @Column(name = "batchSize")
    private Integer batchSize;

    @Column(name = "status")
    private String status;

    @Column(name = "failureMessage")
    private String failureMessage;


    @Column(name = "executed_at")
    @Default
    private LocalDateTime executedAt = LocalDateTime.now();

}
