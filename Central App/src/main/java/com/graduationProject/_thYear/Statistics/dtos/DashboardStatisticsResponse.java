package com.graduationProject._thYear.Statistics.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatisticsResponse {

    private GeneralIndicators generalIndicators;
    private List<BranchPerformance> branchPerformance;
    private ProductAnalytics productAnalytics;
    private FinancialAnalysis financialAnalysis;
    private TimeTrends timeTrends;
    private InventoryInsights inventoryInsights;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeneralIndicators {
        private BigDecimal totalSales;
        private BigDecimal totalQuantitySold;
        private Long totalInvoices;
        private BigDecimal totalPurchases;
        private BigDecimal totalQuantityPurchased;
        private Long totalPurchaseInvoices;
        private BigDecimal totalReturnsValue;
        private BigDecimal totalReturnsQuantity;
        private Long totalReturnInvoices;
        private BigDecimal returnRatePercentage;
        private Long totalBranches;
        private Long totalWarehouses;
        private Long totalPos;
        private Long totalActiveEmployees;
        private Long totalProducts;
        private Long warehouseProducts;
        private Long serviceProducts;
        private BigDecimal totalStockQuantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BranchPerformance {
        private Integer branchId;
        private String branchName;
        private BigDecimal branchSales;
        private BigDecimal branchQuantitySold;
        private Long branchInvoiceCount;
        private BigDecimal shareOfTotalPercentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductAnalytics {
        private List<ProductProfitability> profitableProducts;
        private List<ProductProfitability> unprofitableProducts;
        private List<ProductProfitability> allProductsProfitability;
        private List<ProductMovement> movementAnalysis;
        private Map<String, List<ProductMovement>> movementCategories;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductProfitability {
        private Integer productId;
        private String productName;
        private String productGroup;
        private BigDecimal totalQuantitySold;
        private BigDecimal totalSalesRevenue;
        private BigDecimal totalPurchaseCost;
        private BigDecimal grossProfit;
        private BigDecimal profitMarginPercentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductMovement {
        private Integer productId;
        private String productName;
        private String productGroup;
        private BigDecimal totalQuantitySold;
        private BigDecimal totalSalesValue;
        private Long totalSalesOccurrences;
        private String movementCategory;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FinancialAnalysis {
        private BigDecimal totalRevenue;
        private BigDecimal totalCost;
        private BigDecimal totalReturnsValue;
        private BigDecimal grossProfit;
        private BigDecimal profitMarginPercentage;
        private List<PaymentMethod> paymentMethods;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentMethod {
        private Integer paymentType;
        private Long invoiceCount;
        private BigDecimal totalAmount;
        private BigDecimal averageAmount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeTrends {
        private List<DailyTrend> dailyTrends;
        private List<MonthlyTrend> monthlyTrends;
        private List<PeriodComparison> periodComparison;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyTrend {
        private String saleDate;
        private BigDecimal dailySales;
        private BigDecimal dailyQuantity;
        private Long dailyInvoices;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyTrend {
        private Integer saleYear;
        private Integer saleMonth;
        private String yearMonth;
        private BigDecimal monthlySales;
        private BigDecimal monthlyQuantity;
        private Long monthlyInvoices;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PeriodComparison {
        private String periodType;
        private BigDecimal salesAmount;
        private BigDecimal purchaseAmount;
        private Long salesCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InventoryInsights {
        private List<WarehousePerformance> warehousePerformance;
        private List<InventoryTurnover> inventoryTurnover;
        private List<StockLevel> stockLevels;
        private Map<String, Long> stockStatusSummary;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WarehousePerformance {
        private Integer warehouseId;
        private String warehouseName;
        private String warehouseType;
        private String branchName;
        private BigDecimal totalSales;
        private BigDecimal totalPurchases;
        private BigDecimal totalReturns;
        private Long salesInvoices;
        private Long purchaseInvoices;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InventoryTurnover {
        private Integer productId;
        private String productName;
        private String productGroup;
        private BigDecimal currentStock;
        private BigDecimal totalSoldPeriod;
        private BigDecimal turnoverRatio;
        private BigDecimal daysOfInventory;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockLevel {
        private Integer branchId;
        private String branchName;
        private Integer productId;
        private String productName;
        private Float minQuantity;
        private Float maxQuantity;
        private BigDecimal currentStock;
        private String stockStatus;
    }
}

