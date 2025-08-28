package com.graduationProject._thYear.Statistics.dtos;

import jakarta.persistence.Tuple;
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


        public static GeneralIndicators fromTuple(Tuple tuple) {
            return GeneralIndicators.builder()
                    .totalSales((BigDecimal) tuple.get("total_sales"))
                    .totalInvoices((Long) tuple.get("total_invoices"))
                    .totalPurchases((BigDecimal) tuple.get("total_purchases"))
                    .totalPurchaseInvoices((Long) tuple.get("total_purchase_invoices"))
                    .totalReturnsValue((BigDecimal) tuple.get("total_returns_value"))
                    .totalReturnInvoices((Long) tuple.get("total_return_invoices"))
                    .totalBranches((Long) tuple.get("total_branches"))
                    .totalWarehouses((Long) tuple.get("total_warehouses"))
                    .totalPos((Long) tuple.get("total_pos"))
                    .totalActiveEmployees((Long) tuple.get("total_active_employees"))
                    .totalProducts((Long) tuple.get("total_products"))
                    .warehouseProducts((Long) tuple.get("warehouse_products"))
                    .serviceProducts((Long) tuple.get("service_products"))
                    .totalStockQuantity((BigDecimal) tuple.get("total_stock_quantity"))
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BranchPerformance {
        private Integer branchId;
        private String branchName;
        private BigDecimal branchSales;
//        private BigDecimal branchQuantitySold;
        private Long branchInvoiceCount;
        private BigDecimal shareOfTotalPercentage;


        public static BranchPerformance fromTuple(Tuple tuple) {
            return BranchPerformance.builder()
                    .branchId((Integer) tuple.get("branch_id"))
                    .branchName((String) tuple.get("branch_name"))
                    .branchSales((BigDecimal) tuple.get("branch_sales"))
                    .branchInvoiceCount((Long) tuple.get("branch_invoice_count"))
    //                .shareOfTotalPercentage((BigDecimal) tuple.get("share_of_total_percentage"))
                    .build();
        }
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


        public static ProductProfitability fromTuple(Tuple tuple) {
            return ProductProfitability.builder()
                    .productId((Integer) tuple.get("product_id"))
                    .productName((String) tuple.get("product_name"))
                    .productGroup((String) tuple.get("product_group"))
                    .totalQuantitySold((BigDecimal) tuple.get("total_quantity_sold"))
                    .totalSalesRevenue((BigDecimal) tuple.get("total_sales_revenue"))
                    .totalPurchaseCost((BigDecimal) tuple.get("total_purchase_cost"))
                    .grossProfit((BigDecimal) tuple.get("gross_profit"))
                    .profitMarginPercentage((BigDecimal) tuple.get("profit_margin_percentage"))
                    .build();
        }
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
//        private String movementCategory;

        public static ProductMovement fromTuple(Tuple tuple) {
            return ProductMovement.builder()
                    .productId((Integer) tuple.get("product_id"))
                    .productName((String) tuple.get("product_name"))
                    .productGroup((String) tuple.get("product_group"))
                    .totalQuantitySold((BigDecimal) tuple.get("total_quantity_sold"))
                    .totalSalesValue((BigDecimal) tuple.get("total_sales_value"))
                    .totalSalesOccurrences((Long) tuple.get("total_sales_occurrences"))
                    .build();
        }
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
//        private List<PaymentMethod> paymentMethods;

        public static FinancialAnalysis fromTuple(Tuple tuple) {
            return FinancialAnalysis.builder()
                    .totalRevenue((BigDecimal) tuple.get("total_revenue"))
                    .totalCost((BigDecimal) tuple.get("total_cost"))
                    .totalReturnsValue((BigDecimal) tuple.get("total_returns_value"))
                    .grossProfit((BigDecimal) tuple.get("gross_profit"))
                    .profitMarginPercentage((BigDecimal) tuple.get("profit_margin_percentage"))
                    .build();
        }
    }

//    @Data
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class PaymentMethod {
//        private Integer paymentType;
//        private Long invoiceCount;
//        private BigDecimal totalAmount;
//        private BigDecimal averageAmount;
//    }

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

        public static DailyTrend fromTuple(Tuple tuple) {
            return DailyTrend.builder()
                    .saleDate(tuple.get("sale_date").toString())
                    .dailySales((BigDecimal) tuple.get("daily_sales"))
                    .dailyQuantity((BigDecimal) tuple.get("daily_quantity"))
                    .dailyInvoices((Long) tuple.get("daily_invoices"))
                    .build();
        }
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

        public static MonthlyTrend fromTuple(Tuple tuple) {
            return MonthlyTrend.builder()
                    .saleYear((Integer) tuple.get("sale_year"))
                    .saleMonth((Integer) tuple.get("sale_month"))
                    .yearMonth((String) tuple.get("year_month"))
                    .monthlySales((BigDecimal) tuple.get("monthly_sales"))
                    .monthlyQuantity((BigDecimal) tuple.get("monthly_quantity"))
                    .monthlyInvoices((Long) tuple.get("monthly_invoices"))
                    .build();
        }

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

        public static PeriodComparison fromTuple(Tuple tuple) {
            return PeriodComparison.builder()
                    .periodType((String) tuple.get("period_type"))
                    .salesAmount((BigDecimal) tuple.get("sales_amount"))
                    .purchaseAmount((BigDecimal) tuple.get("purchase_amount"))
                    .salesCount((Long) tuple.get("sales_count"))
                    .build();
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InventoryInsights {
//        private List<WarehousePerformance> warehousePerformance;
//        private List<InventoryTurnover> inventoryTurnover;
        private List<StockLevel> stockLevels;
        private Map<String, Long> stockStatusSummary;
    }

//    @Data
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class WarehousePerformance {
//        private Integer warehouseId;
//        private String warehouseName;
//        private String warehouseType;
//        private String branchName;
//        private BigDecimal totalSales;
//        private BigDecimal totalPurchases;
//        private BigDecimal totalReturns;
//        private Long salesInvoices;
//        private Long purchaseInvoices;
//    }
//
//    @Data
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class InventoryTurnover {
//        private Integer productId;
//        private String productName;
//        private String productGroup;
//        private BigDecimal currentStock;
//        private BigDecimal totalSoldPeriod;
//        private BigDecimal turnoverRatio;
//        private BigDecimal daysOfInventory;
//    }

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

        public static StockLevel fromTuple(Tuple tuple) {
            return StockLevel.builder()
                    .branchId((Integer) tuple.get("branch_id"))
                    .branchName((String) tuple.get("branch_name"))
                    .productId((Integer) tuple.get("product_id"))
                    .productName((String) tuple.get("product_name"))
                    .minQuantity((Float) tuple.get("min_quantity"))
                    .maxQuantity((Float) tuple.get("max_quantity"))
                    .currentStock((BigDecimal) tuple.get("current_stock"))
                    .stockStatus((String) tuple.get("stock_status"))
                    .build();
        }
    }
}

