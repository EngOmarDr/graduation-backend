package com.graduationProject._thYear.EventSyncronization.Response;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SyncStatsResponse {
    Instant nextSyncSchedule;
    List<SyncJobResponse> syncJobs;
    
}
