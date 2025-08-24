//package com.graduationProject._thYear.Statistics.controllers;
//
//
//import com.graduationProject._thYear.Statistics.services.StatisticsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1/statistics")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
//public class StatisticsController {
//
//    private final StatisticsService statisticsService;
//
//    // ==================== GENERAL DASHBOARD ENDPOINTS ====================
//
//    /**
//     * Get comprehensive dashboard statistics for the last month
//     */
//    @GetMapping("/dashboard/last-month")
//    public ResponseEntity<Map<String, Object>> getLastMonthDashboardStatistics() {
//        try {
//            Map<String, Object> statistics = statisticsService.getLastMonthDashboardStatistics();
//            return ResponseEntity.ok(statistics);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Get today's statistics
//     */
//    @GetMapping("/dashboard/today")
//    public ResponseEntity<Map<String, Object>> getTodayStatistics() {
//        try {
//            Map<String, Object> statistics = statisticsService.getTodayStatistics();
//            return ResponseEntity.ok(statistics);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Get this week's statistics
//     */
//    @GetMapping("/dashboard/this-week")
//    public ResponseEntity<Map<String, Object>> getThisWeekStatistics() {
//        try {
//            Map<String, Object> statistics = statisticsService.getThisWeekStatistics();
//            return ResponseEntity.ok(statistics);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Get this month's statistics
//     */
//    @GetMapping("/dashboard/this-month")
//    public ResponseEntity<Map<String, Object>> getThisMonthStatistics() {
//        try {
//            Map<String, Object> statistics = statisticsService.getThisMonthStatistics();
//            return ResponseEntity.ok(statistics);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Get this year's statistics
//     */
//    @GetMapping("/dashboard/this-year")
//    public ResponseEntity<Map<String, Object>> getThisYearStatistics() {
//        try {
//            Map<String, Object> statistics = statisticsService.getThisYearStatistics();
//            return ResponseEntity.ok(statistics);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Get statistics for a custom date range
//     */
//    @GetMapping("/dashboard/custom-range")
//    public ResponseEntity<Map<String, Object>> getCustomRangeStatistics(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
//        try {
//            Map<String, Object> statistics = statisticsService.getStatisticsForDateRange(startDate, endDate);
//            return ResponseEntity.ok(statistics);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    // ==================== SPECIFIC ANALYTICS ENDPOINTS ====================
//
//    /**
//     * Get general indicators for a date range
//     */
//    @GetMapping("/general-indicators")
//    public ResponseEntity<Map<String, Object>> getGeneralIndicators(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
//        try {
//            Map<String, Object> indicators = statisticsService.getGeneralIndicators(startDate, endDate);
//            return ResponseEntity.ok(indicators);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Get branch performance for a date range
//     */
//    @GetMapping("/branch-performance")
//    public ResponseEntity<Map<String, Object>> getBranchPerformance(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
//        try {
//            Map<String, Object> response = Map.of("branch_performance", statisticsService.getBranchPerformance(startDate, endDate));
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Get top-selling products by branch for a date range
//     */
//    @GetMapping("/top-selling-products")
//    public ResponseEntity<Map<String, Object>> getTopSellingProducts(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
//        try {
//            Map<String, Object> response = Map.of("top_selling_products", statisticsService.getTopSellingProductsByBranch(startDate, endDate));
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Get most returned products by branch for a date range
//     */
//    @GetMapping("/most-returned-products")
//    public ResponseEntity<Map<String, Object>> getMostReturnedProducts(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
//        try {
//            Map<String, Object> response = Map.of("most_returned_products", statisticsService.getMostReturnedProductsByBranch(startDate, endDate));
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Get employee performance by branch for a date range
//     */
//    @GetMapping("/employee-performance")
//    public ResponseEntity<Map<String, Object>> getEmployeePerformance(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
//        try {
//            Map<String, Object> response = Map.of("employee_performance", statisticsService.getEmployeePerformanceByBranch(startDate, endDate));
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Get stock levels analysis by branch
//     */
//    @GetMapping("/stock-levels")
//    public ResponseEntity<Map<String, Object>> getStockLevels() {
//        try {
//            Map<String, Object> response = Map.of("stock_levels", statisticsService.getStockLevelsByBranch());
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Get product analytics for a date range
//     */
//    @GetMapping("/product-analytics")
//    public ResponseEntity<Map<String, Object>> getProductAnalytics(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
//        try {
//            Map<String, Object> analytics = statisticsService.getProductAnalytics(startDate, endDate);
//            return ResponseEntity.ok(analytics);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Get financial analysis for a date range
//     */
//    @GetMapping("/financial-analysis")
//    public ResponseEntity<Map<String, Object>> getFinancialAnalysis(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
//        try {
//            Map<String, Object> financial = statisticsService.getFinancialAnalysis(startDate, endDate);
//            return ResponseEntity.ok(financial);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Get time trends for a date range
//     */
//    @GetMapping("/time-trends")
//    public ResponseEntity<Map<String, Object>> getTimeTrends(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
//        try {
//            Map<String, Object> trends = statisticsService.getTimeTrends(startDate, endDate);
//            return ResponseEntity.ok(trends);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Get inventory insights for a date range
//     */
//    @GetMapping("/inventory-insights")
//    public ResponseEntity<Map<String, Object>> getInventoryInsights(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
//        try {
//            Map<String, Object> inventory = statisticsService.getInventoryInsights(startDate, endDate);
//            return ResponseEntity.ok(inventory);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    // ==================== QUICK STATS ENDPOINTS ====================
//
//    /**
//     * Get quick overview statistics (system counts, basic metrics)
//     */
//    @GetMapping("/quick-overview")
//    public ResponseEntity<Map<String, Object>> getQuickOverview() {
//        try {
//            // Get system overview and product overview
//            Map<String, Object> overview = new java.util.HashMap<>();
//
//            // System overview
//            var systemOverview = statisticsService.getGeneralIndicators(
//                    LocalDateTime.now().minusDays(30),
//                    LocalDateTime.now()
//            );
//
//            overview.put("total_branches", systemOverview.get("total_branches"));
//            overview.put("total_warehouses", systemOverview.get("total_warehouses"));
//            overview.put("total_pos", systemOverview.get("total_pos"));
//            overview.put("total_active_employees", systemOverview.get("total_active_employees"));
//            overview.put("total_products", systemOverview.get("total_products"));
//            overview.put("total_stock_quantity", systemOverview.get("total_stock_quantity"));
//
//            return ResponseEntity.ok(overview);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Get sales summary for quick view
//     */
//    @GetMapping("/sales-summary")
//    public ResponseEntity<Map<String, Object>> getSalesSummary(
//            @RequestParam(defaultValue = "30") int days) {
//        try {
//            LocalDateTime endDate = LocalDateTime.now();
//            LocalDateTime startDate = endDate.minusDays(days);
//
//            Map<String, Object> salesSummary = statisticsService.getGeneralIndicators(startDate, endDate);
//
//            // Extract only sales-related metrics
//            Map<String, Object> summary = new java.util.HashMap<>();
//            summary.put("total_sales", salesSummary.get("total_sales"));
//            summary.put("total_quantity_sold", salesSummary.get("total_quantity_sold"));
//            summary.put("total_invoices", salesSummary.get("total_invoices"));
//            summary.put("period_days", days);
//            summary.put("start_date", startDate);
//            summary.put("end_date", endDate);
//
//            return ResponseEntity.ok(summary);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Get performance comparison between periods
//     */
//    @GetMapping("/performance-comparison")
//    public ResponseEntity<Map<String, Object>> getPerformanceComparison(
//            @RequestParam(defaultValue = "30") int currentPeriodDays,
//            @RequestParam(defaultValue = "30") int previousPeriodDays) {
//        try {
//            LocalDateTime now = LocalDateTime.now();
//            LocalDateTime currentEnd = now;
//            LocalDateTime currentStart = now.minusDays(currentPeriodDays);
//            LocalDateTime previousEnd = currentStart;
//            LocalDateTime previousStart = previousEnd.minusDays(previousPeriodDays);
//
//            Map<String, Object> currentPeriod = statisticsService.getGeneralIndicators(currentStart, currentEnd);
//            Map<String, Object> previousPeriod = statisticsService.getGeneralIndicators(previousStart, previousEnd);
//
//            Map<String, Object> comparison = new java.util.HashMap<>();
//            comparison.put("current_period", currentPeriod);
//            comparison.put("previous_period", previousPeriod);
//            comparison.put("current_period_days", currentPeriodDays);
//            comparison.put("previous_period_days", previousPeriodDays);
//
//            return ResponseEntity.ok(comparison);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
//}
//
