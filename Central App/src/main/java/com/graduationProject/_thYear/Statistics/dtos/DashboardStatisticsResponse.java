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

    private static Long toLong(Object raw) {
        if (raw == null) return 0L;
        if (raw instanceof Long l) return l;
        if (raw instanceof Integer i) return i.longValue();
        if (raw instanceof Short s) return s.longValue();
        if (raw instanceof java.math.BigInteger bi) return bi.longValue();
        if (raw instanceof BigDecimal bd) return bd.longValue();
        if (raw instanceof Number n) return n.longValue();
        try { return Long.parseLong(raw.toString()); } catch (Exception e) { return 0L; }
    }

    private static Integer toInt(Object raw) {
        if (raw == null) return null;
        if (raw instanceof Integer i) return i;
        if (raw instanceof Short s) return (int) s;
        if (raw instanceof Long l) return l.intValue();
        if (raw instanceof java.math.BigInteger bi) return bi.intValue();
        if (raw instanceof BigDecimal bd) return bd.intValue();
        if (raw instanceof Number n) return n.intValue();
        try { return Integer.parseInt(raw.toString()); } catch (Exception e) { return null; }
    }

    private static BigDecimal toBigDecimal(Object raw) {
        if (raw == null) return BigDecimal.ZERO;
        if (raw instanceof BigDecimal bd) return bd;
        if (raw instanceof Number n) return BigDecimal.valueOf(n.doubleValue());
        try { return new BigDecimal(raw.toString()); } catch (Exception e) { return BigDecimal.ZERO; }
    }

    private static Float toFloat(Object raw) {
        if (raw == null) return null;
        if (raw instanceof Float f) return f;
        if (raw instanceof Number n) return n.floatValue();
        try { return Float.parseFloat(raw.toString()); } catch (Exception e) { return null; }
    }

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
                    .totalSales(toBigDecimal(tuple.get("total_sales")))
                    .totalInvoices(toLong(tuple.get("total_invoices")))
                    .totalPurchases(toBigDecimal(tuple.get("total_purchases")))
                    .totalPurchaseInvoices(toLong(tuple.get("total_purchase_invoices")))
                    .totalReturnsValue(toBigDecimal(tuple.get("total_returns_value")))
                    .totalReturnInvoices(toLong(tuple.get("total_return_invoices")))
                    .totalBranches(toLong(tuple.get("total_branches")))
                    .totalWarehouses(toLong(tuple.get("total_warehouses")))
                    .totalPos(toLong(tuple.get("total_pos")))
                    .totalActiveEmployees(toLong(tuple.get("total_active_employees")))
                    .totalProducts(toLong(tuple.get("total_products")))
                    .warehouseProducts(toLong(tuple.get("warehouse_products")))
                    .serviceProducts(toLong(tuple.get("service_products")))
                    .totalStockQuantity(toBigDecimal(tuple.get("total_stock_quantity")))
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
    //    private BigDecimal shareOfTotalPercentage;


        public static BranchPerformance fromTuple(Tuple tuple) {
            return BranchPerformance.builder()
                    .branchId(toInt(tuple.get("branch_id")))
                    .branchName((String) tuple.get("branch_name"))
                    .branchSales(toBigDecimal(tuple.get("branch_sales")))
                    .branchInvoiceCount(toLong(tuple.get("branch_invoice_count")))
              //      .shareOfTotalPercentage(toBigDecimal(tuple.get("share_of_total_percentage")))
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
                    .productId(toInt(tuple.get("product_id")))
                    .productName((String) tuple.get("product_name"))
                    .productGroup((String) tuple.get("product_group"))
                    .totalQuantitySold(toBigDecimal(tuple.get("total_quantity_sold")))
                    .totalSalesRevenue(toBigDecimal(tuple.get("total_sales_revenue")))
                    .totalPurchaseCost(toBigDecimal(tuple.get("total_purchase_cost")))
                    .grossProfit(toBigDecimal(tuple.get("gross_profit")))
                    .profitMarginPercentage(toBigDecimal(tuple.get("profit_margin_percentage")))
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
                    .productId(toInt(tuple.get("product_id")))
                    .productName((String) tuple.get("product_name"))
                    .productGroup((String) tuple.get("product_group"))
                    .totalQuantitySold(toBigDecimal(tuple.get("total_quantity_sold")))
                    .totalSalesValue(toBigDecimal(tuple.get("total_sales_value")))
                    .totalSalesOccurrences(toLong(tuple.get("total_sales_occurrences")))
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
                    .totalRevenue(toBigDecimal(tuple.get("total_revenue")))
                    .totalCost(toBigDecimal(tuple.get("total_cost")))
                    .totalReturnsValue(toBigDecimal(tuple.get("total_returns_value")))
                    .grossProfit(toBigDecimal(tuple.get("gross_profit")))
                    .profitMarginPercentage(toBigDecimal(tuple.get("profit_margin_percentage")))
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
                    .dailySales(toBigDecimal(tuple.get("daily_sales")))
                    .dailyQuantity(toBigDecimal(tuple.get("daily_quantity")))
                    .dailyInvoices(toLong(tuple.get("daily_invoices")))
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
                    .saleYear(toInt(tuple.get("sale_year")))
                    .saleMonth(toInt(tuple.get("sale_month")))
                    .yearMonth((String) tuple.get("year_month"))
                    .monthlySales(toBigDecimal(tuple.get("monthly_sales")))
                    .monthlyQuantity(toBigDecimal(tuple.get("monthly_quantity")))
                    .monthlyInvoices(toLong(tuple.get("monthly_invoices")))
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
                    .salesAmount(toBigDecimal(tuple.get("sales_amount")))
                    .purchaseAmount(toBigDecimal(tuple.get("purchase_amount")))
                    .salesCount(toLong(tuple.get("sales_count")))
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
                    .branchId(toInt(tuple.get("branch_id")))
                    .branchName((String) tuple.get("branch_name"))
                    .productId(toInt(tuple.get("product_id")))
                    .productName((String) tuple.get("product_name"))
                    .minQuantity(toFloat(tuple.get("min_quantity")))
                    .maxQuantity(toFloat(tuple.get("max_quantity")))
                    .currentStock(toBigDecimal(tuple.get("current_stock")))
                    .stockStatus((String) tuple.get("stock_status"))
                    .build();
        }
    }
}

