package com.graduationProject._thYear.EventSyncronization.Response;

import java.time.LocalDateTime;

import com.graduationProject._thYear.EventSyncronization.Entities.SyncJob;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SyncJobResponse {
    private Integer id;
    private String topic;
    private Integer batchSize;
    private String status;
    private String failureMessage;
    private LocalDateTime executedAt;

    public static SyncJobResponse fromSyncJobEntity(SyncJob syncjob){
        return SyncJobResponse.builder()
            .id(syncjob.getId())
            .topic(syncjob.getTopic())
            .batchSize(syncjob.getBatchSize())
            .status(syncjob.getStatus())
            .failureMessage(syncjob.getFailureMessage())
            .executedAt(syncjob.getExecutedAt())
            .build();
    }
}
