package com.graduationProject._thYear.Statistics.controllers;


import com.graduationProject._thYear.Statistics.dtos.DashboardStatisticsResponse;
import com.graduationProject._thYear.Statistics.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;


    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatisticsResponse> getDashboardStatistics(
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        LocalDateTime safeEnd = endDate != null ? endDate : LocalDateTime.now();
        LocalDateTime safeStart = startDate != null ? startDate : safeEnd.minusDays(30);

        DashboardStatisticsResponse response = statisticsService.getDashboardStatistics(safeStart, safeEnd);
        return ResponseEntity.ok(response);
    }
}

