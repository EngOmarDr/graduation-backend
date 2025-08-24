package com.graduationProject._thYear.Statistics.services;


import com.graduationProject._thYear.Invoice.repositories.InvoiceHeaderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.persistence.Tuple;

@Service
@RequiredArgsConstructor
public class StatisticsService {


    private final InvoiceHeaderRepository invoiceHeaderRepository;

    // ==================== GENERAL DASHBOARD STATISTICS ====================
//
//    /**
//     * Get comprehensive dashboard statistics for the last month
//     */
//    public Map<String, Object> getLastMonthDashboardStatistics() {
//        LocalDateTime endDate = LocalDateTime.now();
//        LocalDateTime startDate = endDate.minusMonths(1);
//
//        Map<String, Object> dashboard = new HashMap<>();
//
//        // General indicators
//        dashboard.put("general_indicators", getGeneralIndicators(startDate, endDate));
//
//        // Branch performance
//        dashboard.put("branch_performance", getBranchPerformance(startDate, endDate));
//
//        // Product analytics
//        dashboard.put("product_analytics", getProductAnalytics(startDate, endDate));
//
//        // Financial analysis
//        dashboard.put("financial_analysis", getFinancialAnalysis(startDate, endDate));
//
//        // Time trends
//        dashboard.put("time_trends", getTimeTrends(startDate, endDate));
//
//        // Inventory insights
//        dashboard.put("inventory_insights", getInventoryInsights(startDate, endDate));
//
//        return dashboard;
//    }
//
//    /**
//     * Get general indicators across the whole system
//     */
//    public Map<String, Object> getGeneralIndicators(LocalDateTime startDate, LocalDateTime endDate) {
//        Map<String, Object> indicators = new HashMap<>();
//
//        // Sales statistics
//        Tuple salesData = invoiceHeaderRepository.getTotalSales(startDate, endDate);
//        if (salesData != null) {
//            indicators.put("total_sales", salesData.get("total_sales"));
//            indicators.put("total_quantity_sold", salesData.get("total_quantity_sold"));
//            indicators.put("total_invoices", salesData.get("total_invoices"));
//        }
//
//        // Purchase statistics
//        Tuple purchaseData = invoiceHeaderRepository.getTotalPurchases(startDate, endDate);
//        if (purchaseData != null) {
//            indicators.put("total_purchases", purchaseData.get("total_purchases"));
//            indicators.put("total_quantity_purchased", purchaseData.get("total_quantity_purchased"));
//            indicators.put("total_purchase_invoices", purchaseData.get("total_purchase_invoices"));
//        }
//
//        // Returns statistics
////        Tuple returnsData = invoiceHeaderRepository.getTotalReturns(startDate, endDate);
////        if (returnsData != null) {
////            indicators.put("total_returns_value", returnsData.get("total_returns_value"));
////            indicators.put("total_returns_quantity", returnsData.get("total_returns_quantity"));
////            indicators.put("total_return_invoices", returnsData.get("total_return_invoices"));
////
////            // Calculate return rate
////            BigDecimal totalSales = (BigDecimal) salesData.get("total_sales");
////            BigDecimal totalReturns = (BigDecimal) returnsData.get("total_returns_value");
////            if (totalSales != null && totalSales.compareTo(BigDecimal.ZERO) > 0) {
////                BigDecimal returnRate = totalReturns.divide(totalSales, 4, BigDecimal.ROUND_HALF_UP)
////                        .multiply(new BigDecimal("100"));
////                indicators.put("return_rate_percentage", returnRate);
////            }
////        }
//
//        // System overview
//        Tuple systemOverview = invoiceHeaderRepository.getSystemOverview();
//        if (systemOverview != null) {
//            indicators.put("total_branches", systemOverview.get("total_branches"));
//            indicators.put("total_warehouses", systemOverview.get("total_warehouses"));
//            indicators.put("total_pos", systemOverview.get("total_pos"));
//            indicators.put("total_active_employees", systemOverview.get("total_active_employees"));
//        }
//
//        // Product overview
//        Tuple productOverview = invoiceHeaderRepository.getProductOverview();
//        if (productOverview != null) {
//            indicators.put("total_products", productOverview.get("total_products"));
//            indicators.put("warehouse_products", productOverview.get("warehouse_products"));
//            indicators.put("service_products", productOverview.get("service_products"));
//            indicators.put("total_stock_quantity", productOverview.get("total_stock_quantity"));
//        }
//
//        return indicators;
//    }
//
//    /**
//     * Get branch performance statistics
//     */
//    public List<Map<String, Object>> getBranchPerformance(LocalDateTime startDate, LocalDateTime endDate) {
//        List<Tuple> branchData = invoiceHeaderRepository.getBranchSalesPerformance(startDate, endDate);
//        List<Map<String, Object>> branchPerformance = new ArrayList<>();
//
//        BigDecimal totalSystemSales = BigDecimal.ZERO;
//
//        // First pass to calculate total system sales
//        for (Tuple branch : branchData) {
//            BigDecimal branchSales = (BigDecimal) branch.get("branch_sales");
//            if (branchSales != null) {
//                totalSystemSales = totalSystemSales.add(branchSales);
//            }
//        }
//
//        // Second pass to add percentage calculations
//        for (Tuple branch : branchData) {
//            Map<String, Object> branchInfo = new HashMap<>();
//            branchInfo.put("branch_id", branch.get("branch_id"));
//            branchInfo.put("branch_name", branch.get("branch_name"));
//            branchInfo.put("branch_sales", branch.get("branch_sales"));
//            branchInfo.put("branch_quantity_sold", branch.get("branch_quantity_sold"));
//            branchInfo.put("branch_invoice_count", branch.get("branch_invoice_count"));
//
//            // Calculate share of total sales
//            BigDecimal branchSales = (BigDecimal) branch.get("branch_sales");
//            if (branchSales != null && totalSystemSales.compareTo(BigDecimal.ZERO) > 0) {
//                BigDecimal sharePercentage = branchSales.divide(totalSystemSales, 4, BigDecimal.ROUND_HALF_UP)
//                        .multiply(new BigDecimal("100"));
//                branchInfo.put("share_of_total_percentage", sharePercentage);
//            }
//
//            branchPerformance.add(branchInfo);
//        }
//
//        return branchPerformance;
//    }
//
//    /**
//     * Get top-selling products by branch
//     */
////    public List<Map<String, Object>> getTopSellingProductsByBranch(LocalDateTime startDate, LocalDateTime endDate) {
////        List<Tuple> productData = invoiceHeaderRepository.getTopSellingProductsByBranch(startDate, endDate);
////        return productData.stream()
////                .map(this::convertTupleToMap)
////                .collect(Collectors.toList());
////    }
//
//    /**
//     * Get most returned products by branch
//     */
////    public List<Map<String, Object>> getMostReturnedProductsByBranch(LocalDateTime startDate, LocalDateTime endDate) {
////        List<Tuple> returnData = invoiceHeaderRepository.getMostReturnedProductsByBranch(startDate, endDate);
////        return returnData.stream()
////                .map(this::convertTupleToMap)
////                .collect(Collectors.toList());
////    }
//
//    /**
//     * Get employee performance by branch
//     */
//    public List<Map<String, Object>> getEmployeePerformanceByBranch(LocalDateTime startDate, LocalDateTime endDate) {
//        List<Tuple> employeeData = invoiceHeaderRepository.getEmployeePerformanceByBranch(startDate, endDate);
//        return employeeData.stream()
//                .map(this::convertTupleToMap)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Get stock levels analysis by branch
//     */
//    public List<Map<String, Object>> getStockLevelsByBranch() {
//        List<Tuple> stockData = invoiceHeaderRepository.getStockLevelsByBranch();
//        return stockData.stream()
//                .map(this::convertTupleToMap)
//                .collect(Collectors.toList());
//    }
//
//    // ==================== PRODUCT ANALYTICS ====================
//
//    /**
//     * Get product analytics including profitability and movement analysis
//     */
//    public Map<String, Object> getProductAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
//        Map<String, Object> analytics = new HashMap<>();
//
//        // Product profitability analysis
//        List<Tuple> profitabilityData = invoiceHeaderRepository.getProductProfitabilityAnalysis(startDate, endDate);
//        List<Map<String, Object>> profitabilityList = profitabilityData.stream()
//                .map(this::convertTupleToMap)
//                .collect(Collectors.toList());
//
//        // Categorize products by profitability
//        List<Map<String, Object>> profitableProducts = new ArrayList<>();
//        List<Map<String, Object>> unprofitableProducts = new ArrayList<>();
//
//        for (Map<String, Object> product : profitabilityList) {
//            BigDecimal profitMargin = (BigDecimal) product.get("profit_margin_percentage");
//            if (profitMargin != null && profitMargin.compareTo(BigDecimal.ZERO) > 0) {
//                profitableProducts.add(product);
//            } else {
//                unprofitableProducts.add(product);
//            }
//        }
//
//        analytics.put("profitable_products", profitableProducts);
//        analytics.put("unprofitable_products", unprofitableProducts);
//        analytics.put("all_products_profitability", profitabilityList);
////
////        // Product movement analysis
////        List<Tuple> movementData = invoiceHeaderRepository.getProductMovementAnalysis(startDate, endDate);
////        List<Map<String, Object>> movementList = movementData.stream()
////                .map(this::convertTupleToMap)
////                .collect(Collectors.toList());
////
////        // Categorize by movement speed
////        Map<String, List<Map<String, Object>>> movementCategories = movementList.stream()
////                .collect(Collectors.groupingBy(
////                        product -> (String) product.get("movement_category")
////                ));
////
////        analytics.put("movement_analysis", movementList);
////        analytics.put("movement_categories", movementCategories);
//
//        return analytics;
//    }
//
//    // ==================== FINANCIAL ANALYSIS ====================
//
//    /**
//     * Get comprehensive financial analysis
//     */
//    public Map<String, Object> getFinancialAnalysis(LocalDateTime startDate, LocalDateTime endDate) {
//        Map<String, Object> financial = new HashMap<>();
//
//        // Main financial metrics
//        Tuple financialData = invoiceHeaderRepository.getFinancialAnalysis(startDate, endDate);
//        if (financialData != null) {
//            financial.put("total_revenue", financialData.get("total_revenue"));
//            financial.put("total_cost", financialData.get("total_cost"));
//            financial.put("total_returns_value", financialData.get("total_returns_value"));
//            financial.put("gross_profit", financialData.get("gross_profit"));
//            financial.put("profit_margin_percentage", financialData.get("profit_margin_percentage"));
//        }
//
////        // Payment method analysis
////        List<Tuple> paymentData = invoiceHeaderRepository.getPaymentMethodAnalysis(startDate, endDate);
////        List<Map<String, Object>> paymentList = paymentData.stream()
////                .map(this::convertTupleToMap)
////                .collect(Collectors.toList());
////        financial.put("payment_methods", paymentList);
//
//        return financial;
//    }
//
//    // ==================== TIME TRENDS ====================
//
//    /**
//     * Get time-based trend analysis
//     */
//    public Map<String, Object> getTimeTrends(LocalDateTime startDate, LocalDateTime endDate) {
//        Map<String, Object> trends = new HashMap<>();
//
//        // Daily sales trends
//        List<Tuple> dailyData = invoiceHeaderRepository.getSalesTrendAnalysis(startDate, endDate);
//        List<Map<String, Object>> dailyTrends = dailyData.stream()
//                .map(this::convertTupleToMap)
//                .collect(Collectors.toList());
//        trends.put("daily_trends", dailyTrends);
//
//        // Monthly comparison
//        List<Tuple> monthlyData = invoiceHeaderRepository.getMonthlySalesComparison(startDate, endDate);
//        List<Map<String, Object>> monthlyTrends = monthlyData.stream()
//                .map(this::convertTupleToMap)
//                .collect(Collectors.toList());
//        trends.put("monthly_trends", monthlyTrends);
////
////        // Period comparison
////        List<Tuple> periodData = invoiceHeaderRepository.getPeriodComparison(startDate, endDate);
////        List<Map<String, Object>> periodComparison = periodData.stream()
////                .map(this::convertTupleToMap)
////                .collect(Collectors.toList());
////        trends.put("period_comparison", periodComparison);
//
//        return trends;
//    }
//
//    // ==================== INVENTORY INSIGHTS ====================
//
//    /**
//     * Get inventory and warehouse insights
//     */
//    public Map<String, Object> getInventoryInsights(LocalDateTime startDate, LocalDateTime endDate) {
//        Map<String, Object> inventory = new HashMap<>();
////
////        // Warehouse performance comparison
////        List<Tuple> warehouseData = invoiceHeaderRepository.getWarehousePerformanceComparison(startDate, endDate);
////        List<Map<String, Object>> warehousePerformance = warehouseData.stream()
////                .map(this::convertTupleToMap)
////                .collect(Collectors.toList());
////        inventory.put("warehouse_performance", warehousePerformance);
////
////        // Inventory turnover analysis
////        List<Tuple> turnoverData = invoiceHeaderRepository.getInventoryTurnoverAnalysis(startDate, endDate);
////        List<Map<String, Object>> turnoverAnalysis = turnoverData.stream()
////                .map(this::convertTupleToMap)
////                .collect(Collectors.toList());
////        inventory.put("inventory_turnover", turnoverAnalysis);
//
//        // Stock levels by branch
//        List<Map<String, Object>> stockLevels = getStockLevelsByBranch();
//        inventory.put("stock_levels", stockLevels);
//
//        // Stock status summary
//        Map<String, Long> stockStatusSummary = stockLevels.stream()
//                .collect(Collectors.groupingBy(
//                        stock -> (String) stock.get("stock_status"),
//                        Collectors.counting()
//                ));
//        inventory.put("stock_status_summary", stockStatusSummary);
//
//        return inventory;
//    }
//
//    // ==================== HELPER METHODS ====================
//
//    /**
//     * Convert JPA Tuple to Map for easier handling
//     */
//    private Map<String, Object> convertTupleToMap(Tuple tuple) {
//        Map<String, Object> map = new HashMap<>();
//        for (int i = 0; i < tuple.getElements().size(); i++) {
//            String alias = tuple.getElements().get(i).getAlias();
//            Object value = tuple.get(i);
//            map.put(alias, value);
//        }
//        return map;
//    }
//
//    /**
//     * Get statistics for a specific date range
//     */
//    public Map<String, Object> getStatisticsForDateRange(LocalDateTime startDate, LocalDateTime endDate) {
//        Map<String, Object> statistics = new HashMap<>();
//
//        statistics.put("general_indicators", getGeneralIndicators(startDate, endDate));
//        statistics.put("branch_performance", getBranchPerformance(startDate, endDate));
//        statistics.put("product_analytics", getProductAnalytics(startDate, endDate));
//        statistics.put("financial_analysis", getFinancialAnalysis(startDate, endDate));
//        statistics.put("time_trends", getTimeTrends(startDate, endDate));
//        statistics.put("inventory_insights", getInventoryInsights(startDate, endDate));
//
//        return statistics;
//    }
//
//    /**
//     * Get today's statistics
//     */
//    public Map<String, Object> getTodayStatistics() {
//        LocalDateTime endDate = LocalDateTime.now();
//        LocalDateTime startDate = endDate.toLocalDate().atStartOfDay();
//        return getStatisticsForDateRange(startDate, endDate);
//    }
//
//    /**
//     * Get this week's statistics
//     */
//    public Map<String, Object> getThisWeekStatistics() {
//        LocalDateTime endDate = LocalDateTime.now();
//        LocalDateTime startDate = endDate.toLocalDate().minusDays(7).atStartOfDay();
//        return getStatisticsForDateRange(startDate, endDate);
//    }
//
//    /**
//     * Get this month's statistics
//     */
//    public Map<String, Object> getThisMonthStatistics() {
//        LocalDateTime endDate = LocalDateTime.now();
//        LocalDateTime startDate = endDate.minusMonths(1);
//        return getStatisticsForDateRange(startDate, endDate);
//    }
//
//    /**
//     * Get this year's statistics
//     */
//    public Map<String, Object> getThisYearStatistics() {
//        LocalDateTime endDate = LocalDateTime.now();
//        LocalDateTime startDate = endDate.toLocalDate().withDayOfYear(1).atStartOfDay();
//        return getStatisticsForDateRange(startDate, endDate);
//    }
}

