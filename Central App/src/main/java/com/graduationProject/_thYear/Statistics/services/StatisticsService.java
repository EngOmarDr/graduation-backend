package com.graduationProject._thYear.Statistics.services;


import com.graduationProject._thYear.Invoice.repositories.InvoiceHeaderRepository;

import com.graduationProject._thYear.Statistics.dtos.DashboardStatisticsResponse;
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

    public DashboardStatisticsResponse getDashboardStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        // General Indicators
        Tuple totalSales = invoiceHeaderRepository.getTotalSales(startDate, endDate);
        Tuple totalPurchases = invoiceHeaderRepository.getTotalPurchases(startDate, endDate);
        Tuple systemOverview = invoiceHeaderRepository.getSystemOverview();
        Tuple productOverview = invoiceHeaderRepository.getProductOverview();

        DashboardStatisticsResponse.GeneralIndicators generalIndicators = DashboardStatisticsResponse.GeneralIndicators.builder()
                .totalSales(getBigDecimal(totalSales, "total_sales"))
                .totalInvoices(getLong(totalSales, "total_invoices"))
                .totalPurchases(getBigDecimal(totalPurchases, "total_purchases"))
                .totalPurchaseInvoices(getLong(totalPurchases, "total_purchase_invoices"))
                .totalReturnsValue(getBigDecimal(totalPurchases, "total_returns_value"))
                .totalReturnInvoices(getLong(totalPurchases, "total_return_invoices"))
                .returnRatePercentage(getBigDecimal(totalPurchases, "return_rate_percentage"))
                .totalBranches(getLong(systemOverview, "total_branches"))
                .totalWarehouses(getLong(systemOverview, "total_warehouses"))
                .totalPos(getLong(systemOverview, "total_pos"))
                .totalActiveEmployees(getLong(systemOverview, "total_active_employees"))
                .totalProducts(getLong(productOverview, "total_products"))
                .warehouseProducts(getLong(productOverview, "warehouse_products"))
                .serviceProducts(getLong(productOverview, "service_products"))
                .totalStockQuantity(getBigDecimal(productOverview, "total_stock_quantity"))
                .build();

        // Branch Performance
        List<Tuple> branchTuples = invoiceHeaderRepository.getBranchSalesPerformance(startDate, endDate);
        List<DashboardStatisticsResponse.BranchPerformance> branchPerformance = branchTuples == null ?
                List.of() : branchTuples.stream()
                .map(DashboardStatisticsResponse.BranchPerformance::fromTuple)
                .toList();

        // Product Analytics
        List<Tuple> productProfitabilityTuples = invoiceHeaderRepository.getProductProfitabilityAnalysis(startDate, endDate);
        List<DashboardStatisticsResponse.ProductProfitability> allProductsProfitability = productProfitabilityTuples == null ?
                List.of() : productProfitabilityTuples.stream()
                .map(DashboardStatisticsResponse.ProductProfitability::fromTuple)
                .toList();

        List<DashboardStatisticsResponse.ProductProfitability> profitableProducts = allProductsProfitability.stream()
                .filter(p -> safeBigDecimal(p.getProfitMarginPercentage()).compareTo(BigDecimal.ZERO) > 0)
                .toList();

        List<DashboardStatisticsResponse.ProductProfitability> unprofitableProducts = allProductsProfitability.stream()
                .filter(p -> safeBigDecimal(p.getProfitMarginPercentage()).compareTo(BigDecimal.ZERO) <= 0)
                .toList();

        DashboardStatisticsResponse.ProductAnalytics productAnalytics = DashboardStatisticsResponse.ProductAnalytics.builder()
                .allProductsProfitability(allProductsProfitability)
                .profitableProducts(profitableProducts)
                .unprofitableProducts(unprofitableProducts)
                .build();

        // Financial Analysis
        Tuple financialTuple = invoiceHeaderRepository.getFinancialAnalysis(startDate, endDate);
        DashboardStatisticsResponse.FinancialAnalysis financialAnalysis = financialTuple == null ?
                DashboardStatisticsResponse.FinancialAnalysis.builder().build() :
                DashboardStatisticsResponse.FinancialAnalysis.fromTuple(financialTuple);

        // Time Trends
        List<Tuple> dailyTrendsTuples = invoiceHeaderRepository.getSalesTrendAnalysis(startDate, endDate);
        List<DashboardStatisticsResponse.DailyTrend> dailyTrends = dailyTrendsTuples == null ?
                List.of() : dailyTrendsTuples.stream()
                .map(DashboardStatisticsResponse.DailyTrend::fromTuple)
                .toList();

        List<Tuple> monthlyTrendsTuples = invoiceHeaderRepository.getMonthlySalesComparison(startDate, endDate);
        List<DashboardStatisticsResponse.MonthlyTrend> monthlyTrends = monthlyTrendsTuples == null ?
                List.of() : monthlyTrendsTuples.stream()
                .map(DashboardStatisticsResponse.MonthlyTrend::fromTuple)
                .toList();

        DashboardStatisticsResponse.TimeTrends timeTrends = DashboardStatisticsResponse.TimeTrends.builder()
                .dailyTrends(dailyTrends)
                .monthlyTrends(monthlyTrends)
                .build();

        // Inventory Insights
        List<Tuple> stockLevelsTuples = invoiceHeaderRepository.getStockLevelsByBranch();
        List<DashboardStatisticsResponse.StockLevel> stockLevels = stockLevelsTuples == null ?
                List.of() : stockLevelsTuples.stream()
                .map(DashboardStatisticsResponse.StockLevel::fromTuple)
                .toList();

        Map<String, Long> stockStatusSummary = stockLevels.stream()
                .collect(Collectors.groupingBy(
                        DashboardStatisticsResponse.StockLevel::getStockStatus,
                        Collectors.counting()
                ));

        DashboardStatisticsResponse.InventoryInsights inventoryInsights = DashboardStatisticsResponse.InventoryInsights.builder()
                .stockLevels(stockLevels)
                .stockStatusSummary(stockStatusSummary)
                .build();

        // Build Final Response
        return DashboardStatisticsResponse.builder()
                .generalIndicators(generalIndicators)
                .branchPerformance(branchPerformance)
                .productAnalytics(productAnalytics)
                .financialAnalysis(financialAnalysis)
                .timeTrends(timeTrends)
                .inventoryInsights(inventoryInsights)
                .build();
    }

    private BigDecimal getBigDecimal(Tuple tuple, String alias) {
        if (tuple == null) return BigDecimal.ZERO;
        BigDecimal value = nullSafeGet(tuple, alias, BigDecimal.class);
        return value != null ? value : BigDecimal.ZERO;
    }

    private Long getLong(Tuple tuple, String alias) {
        if (tuple == null) return 0L;
        Long value = nullSafeGet(tuple, alias, Long.class);
        return value != null ? value : 0L;
    }

    private <T> T nullSafeGet(Tuple tuple, String alias, Class<T> type) {
        try {
            return tuple.get(alias, type);
        } catch (Exception ignored) {
            return null;
        }
    }

    private BigDecimal safeBigDecimal(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
