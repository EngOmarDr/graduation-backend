package com.graduationProject._thYear.EventSyncronization.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.graduationProject._thYear.EventSyncronization.Entities.SyncJob;
import com.graduationProject._thYear.EventSyncronization.Repositories.SyncJobRepository;
import com.graduationProject._thYear.EventSyncronization.Response.SyncJobResponse;
import com.graduationProject._thYear.EventSyncronization.Response.SyncStatsResponse;
import com.graduationProject._thYear.EventSyncronization.Service.EventSyncService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("api/event-sync")
public class EventSyncController {
    
    @Autowired
    EventSyncService eventSyncService;

    @Autowired
    SyncJobRepository syncJobRepository;
    @PostMapping("/sync-job")
    public String createSyncJob() {
        eventSyncService.createSyncJob();
        return "Sync Job creatd Succssfully";
    }

    @GetMapping("/sync-stats")
    public SyncStatsResponse getSync() {
        CronTrigger cronTrigger = new CronTrigger("* * 2 * * *");
        Instant instant  = cronTrigger.nextExecution(new SimpleTriggerContext());
        List<SyncJob> syncJobs = syncJobRepository.findAll(Sort.by("executedAt").descending());
        return SyncStatsResponse.builder()
            .nextSyncSchedule(instant)
            .syncJobs(syncJobs.stream()
                .map(job -> SyncJobResponse.fromSyncJobEntity(job))
                .collect(Collectors.toList()))
            .build();
    }
    
}
