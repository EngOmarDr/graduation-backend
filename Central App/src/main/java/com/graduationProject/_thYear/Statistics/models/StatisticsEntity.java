package com.graduationProject._thYear.Statistics.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dummy_statistics")
public class StatisticsEntity {
    @Id
    private Long id;



    //    /**
//     * Total sales for different time periods with comparison to previous periods
//     */
//    @Query(value = """
//        SELECT
//            SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as total_sales,
//            COUNT(CASE WHEN ty.type = 'sale' THEN ih.id END) as total_invoices,
//            :startDate as period_start,
//            :endDate as period_end
//        FROM InvoiceHeader ih
//        JOIN ih.invoiceItems item
//        JOIN ih.invoiceType ty
//        WHERE ih.date BETWEEN :startDate AND :endDate
//        AND ih.isPosted = true AND ih.isSuspended = false
//        """)
//    Tuple getTotalSales(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
//
//    /**
//     * Total purchases for different time periods
//     */
//    @Query(value = """
//        SELECT
//            SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as total_purchases,
//            COUNT(CASE WHEN ty.type = 'buy' THEN ih.id END) as total_purchase_invoices,
//            :startDate as period_start,
//            :endDate as period_end
//        FROM InvoiceHeader ih
//        JOIN ih.invoiceItems item
//        JOIN ih.invoiceType ty
//        WHERE ih.date BETWEEN :startDate AND :endDate
//        AND ih.isPosted = true AND ih.isSuspended = false
//        """)
//    Tuple getTotalPurchases(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

//
//    /**
//     * System overview counts (branches, warehouses, POS, active employees)
//     */
//    @Query(value = """
//        SELECT
//            (SELECT COUNT(*) FROM Branch) as total_branches,
//            (SELECT COUNT(*) FROM Warehouse WHERE type = 'WAREHOUSE') as total_warehouses,
//            (SELECT COUNT(*) FROM Warehouse WHERE type = 'POS') as total_pos,
//            (SELECT COUNT(*) FROM user WHERE warehouse_id IS NOT NULL) as total_active_employees
//        """)
//    Tuple getSystemOverview();
//
//    /**
//     * Total products and stock overview
//     */
//    @Query(value = """
//        SELECT
//            (SELECT COUNT(*) FROM Product) as total_products,
//            (SELECT COUNT(*) FROM Product WHERE type = 0) as warehouse_products,
//            (SELECT COUNT(*) FROM Product WHERE type = 1) as service_products,
//            (SELECT COALESCE(SUM(ps.quantity), 0) FROM ProductStock ps) as total_stock_quantity
//        """)
//    Tuple getProductOverview();
//
//    // ==================== BRANCH-LEVEL STATISTICS ====================
//
//    /**
//     * Branch sales performance with share of total
//     */
//    @Query(value = """
//        SELECT
//            b.id as branch_id,
//            b.name as branch_name,
//            SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as branch_sales,
//            COUNT(CASE WHEN ty.type = 'sale' THEN ih.id END) as branch_invoice_count,
//            :startDate as period_start,
//            :endDate as period_end
//        FROM Branch b
//         JOIN Warehouse w ON w.branch.id = b.id
//         JOIN InvoiceHeader ih ON ih.warehouse.id = w.id
//         JOIN ih.invoiceItems item ON item.invoiceHeader.id = ih.id
//         JOIN ih.invoiceType ty ON ty.id = ih.invoiceType.id
//        WHERE (ih.date BETWEEN :startDate AND :endDate OR ih.date IS NULL)
//        AND ih.isPosted = true AND ih.isSuspended = false
//        GROUP BY b.id, b.name
//        ORDER BY branch_sales DESC
//        """)
//    List<Tuple> getBranchSalesPerformance(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

//    /**
//     * Top-selling products by branch
//     */
//    @Query(value = """
//        SELECT
//            b.id as branch_id,
//            b.name as branch_name,
//            p.id as product_id,
//            p.name as product_name,
//            SUM(CASE WHEN ty.type = 'sale' THEN item.qty * item.unitFact ELSE 0 END) as total_quantity_sold,
//            SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as total_sales_value,
//            ROW_NUMBER() OVER (PARTITION BY b.id ORDER BY SUM(CASE WHEN ty.type = 'sale' THEN item.qty * item.unitFact ELSE 0 END) DESC) as rank_in_branch
//        FROM Branch b
//        JOIN Warehouse w ON w.branch.id = b.id
//        JOIN InvoiceHeader ih ON ih.warehouse.id = w.id
//        JOIN ih.invoiceItems item ON item.invoiceHeader.id = ih.id
//        JOIN ih.invoiceType ty ON ty.id = ih.invoiceType.id
//        JOIN item.product p ON p.id = item.product.id
//        WHERE ih.date BETWEEN :startDate AND :endDate
//        AND ih.isPosted = true AND ih.isSuspended = false
//        AND ty.type = 'sale'
//        GROUP BY b.id, b.name, p.id, p.name
//        HAVING rank_in_branch <= 5
//        ORDER BY b.id, rank_in_branch
//        """)
//    List<Tuple> getTopSellingProductsByBranch(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

//    /**
//     * Most returned or slow-moving products by branch
//     */
//    @Query(value = """
//        SELECT
//            b.id as branch_id,
//            b.name as branch_name,
//            p.id as product_id,
//            p.name as product_name,
//            SUM(CASE WHEN ty.type IN ( 'retrieve_sale') THEN item.qty * item.unitFact ELSE 0 END) as total_returns,
//            SUM(CASE WHEN ty.type IN ( 'retrieve_sale') THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as total_returns_value,
//            ROW_NUMBER() OVER (PARTITION BY b.id ORDER BY SUM(CASE WHEN ty.type IN ('retrieve_sale') THEN item.qty * item.unitFact ELSE 0 END) DESC) as return_rank
//        FROM Branch b
//        JOIN Warehouse w ON w.branch.id = b.id
//        JOIN InvoiceHeader ih ON ih.warehouse.id = w.id
//        JOIN ih.invoiceItems item ON item.invoiceHeader.id = ih.id
//        JOIN ih.invoiceType ty ON ty.id = ih.invoiceType.id
//        JOIN item.product p ON p.id = item.product.id
//        WHERE ih.date BETWEEN :startDate AND :endDate
//        AND ih.isPosted = true AND ih.isSuspended = false
//        AND ty.type IN ('retrieve_sale')
//        GROUP BY b.id, b.name, p.id, p.name
//        HAVING return_rank <= 5
//        ORDER BY b.id, return_rank
//        """)
//    List<Tuple> getMostReturnedProductsByBranch(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

//    /**
//     * Employee/cashier performance by branch
//     */
//    @Query(value = """
//            SELECT
//                b.id as branch_id,
//                b.name as branch_name,
//                u.id as user_id,
//                CONCAT(u.firstName, ' ', u.lastName) as employee_name,
//                u.role as employee_role,
//                COUNT(CASE WHEN ty.type = 'sale' THEN ih.id END) as invoices_processed,
//                SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty * item.unitFact ELSE 0 END) as total_sales_amount,
//                SUM(CASE WHEN ty.type = 'sale' THEN item.qty * item.unitFact ELSE 0 END) as total_quantity_sold,
//                :startDate as period_start,
//                :endDate as period_end
//            FROM Branch b
//            JOIN Warehouse w ON w.branch.id = b.id
//            JOIN user u ON u.warehouse.id = w.id
//            LEFT JOIN InvoiceHeader ih ON ih.user.id = u.id
//            LEFT JOIN ih.invoiceItems item ON item.invoiceHeader.id = ih.id
//            LEFT JOIN ih.invoiceType ty ON ty.id = ih.invoiceType.id
//            WHERE (ih.date BETWEEN :startDate AND :endDate OR ih.date IS NULL)
//            AND ih.isPosted = true AND ih.isSuspended = false
//            GROUP BY b.id, b.name, u.id, u.firstName, u.lastName, u.role
//            ORDER BY b.id, total_sales_amount DESC
//            """)
//    List<Tuple> getEmployeePerformanceByBranch(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

//    /**
//     * Stock levels analysis by branch (Low/Sufficient/Overstocked)
//     */
//    @Query(value = """
//        SELECT
//            b.id as branch_id,
//            b.name as branch_name,
//            p.id as product_id,
//            p.name as product_name,
//            p.minQty as min_quantity,
//            p.maxQty as max_quantity,
//            COALESCE(ps.quantity, 0) as current_stock,
//            CASE
//                WHEN COALESCE(ps.quantity, 0) <= p.minQty THEN 'LOW'
//                WHEN COALESCE(ps.quantity, 0) >= p.maxQty THEN 'OVERSTOCKED'
//                ELSE 'SUFFICIENT'
//            END as stock_status
//        FROM Branch b
//        JOIN Warehouse w ON w.branch.id = b.id
//        JOIN Product p ON p.type = 0
//        LEFT JOIN ProductStock ps ON ps.product.id = p.id AND ps.warehouse.id = w.id
//        WHERE w.type = 'WAREHOUSE'
//        ORDER BY b.id, stock_status, current_stock
//        """)
//    List<Tuple> getStockLevelsByBranch();

//    // ==================== PRODUCT ANALYTICS ====================
//
//    /**
//     * Product performance analysis (profitable vs unprofitable)
//     */
//    @Query(value = """
//        SELECT
//            p.id as product_id,
//            p.name as product_name,
//            p.groupId.name as product_group,
//            SUM(CASE WHEN ty.type = 'sale' THEN item.qty * item.unitFact ELSE 0 END) as total_quantity_sold,
//            SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as total_sales_revenue,
//            SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as total_purchase_cost,
//            (SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) -
//             SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END)) as gross_profit,
//            CASE
//                WHEN SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) > 0
//                THEN ((SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) -
//                       SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END)) /
//                       SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END)) * 100
//                ELSE 0
//            END as profit_margin_percentage,
//            :startDate as period_start,
//            :endDate as period_end
//        FROM Product p
//        LEFT JOIN p.groupId
//        LEFT JOIN InvoiceHeader ih ON ih.date BETWEEN :startDate AND :endDate
//        LEFT JOIN ih.invoiceItems item ON item.product.id = p.id
//        LEFT JOIN ih.invoiceType ty ON ty.id = ih.invoiceType.id
//        WHERE (ih.isPosted = true AND ih.isSuspended = false)
//        GROUP BY p.id, p.name, p.groupId.name
//        HAVING total_quantity_sold > 0
//        ORDER BY gross_profit DESC
//        """)
//    List<Tuple> getProductProfitabilityAnalysis(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    // ==================== TIME-BASED TREND ANALYSIS ====================

//    /**
//     * Sales trend analysis over time (daily, weekly, monthly)
//     */
//    @Query(value = """
//        SELECT
//            DATE(ih.date) as sale_date,
//            SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as daily_sales,
//            SUM(CASE WHEN ty.type = 'sale' THEN item.qty  ELSE 0 END) as daily_quantity,
//            COUNT(CASE WHEN ty.type = 'sale' THEN ih.id END) as daily_invoices,
//            :startDate as period_start,
//            :endDate as period_end
//        FROM InvoiceHeader ih
//        JOIN ih.invoiceItems item
//        JOIN ih.invoiceType ty
//        WHERE ih.date BETWEEN :startDate AND :endDate
//        AND ih.isPosted = true AND ih.isSuspended = false
//        AND ty.type = 'sale'
//        GROUP BY DATE(ih.date)
//        ORDER BY sale_date
//        """)
//    List<Tuple> getSalesTrendAnalysis(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

//    /**
//     * Monthly comparison for trend analysis
//     */
//    @Query(value = """
//        SELECT
//            YEAR(ih.date) as sale_year,
//            MONTH(ih.date) as sale_month,
//            CONCAT(YEAR(ih.date), '-', (MONTH(ih.date), 2, '0')) as year_month,
//            SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as monthly_sales,
//            SUM(CASE WHEN ty.type = 'sale' THEN item.qty  ELSE 0 END) as monthly_quantity,
//            COUNT(CASE WHEN ty.type = 'sale' THEN ih.id END) as monthly_invoices,
//            :startDate as period_start,
//            :endDate as period_end
//        FROM InvoiceHeader ih
//        JOIN ih.invoiceItems item
//        JOIN ih.invoiceType ty
//        WHERE ih.date BETWEEN :startDate AND :endDate
//        AND ih.isPosted = true AND ih.isSuspended = false
//        AND ty.type = 'sale'
//        GROUP BY YEAR(ih.date), MONTH(ih.date)
//        ORDER BY sale_year, sale_month
//        """)
//    List<Tuple> getMonthlySalesComparison(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    //    /**
//     * Payment method analysis
//     */
//    @Query(value = """
//        SELECT
//            ih.payType as payment_type,
//            COUNT(*) as invoice_count,
//            SUM(ih.total) as total_amount,
//            AVG(ih.total) as average_amount,
//            :startDate as period_start,
//            :endDate as period_end
//        FROM InvoiceHeader ih
//        WHERE ih.date BETWEEN :startDate AND :endDate
//        AND ih.isPosted = true AND ih.isSuspended = false
//        GROUP BY ih.payType
//        ORDER BY total_amount DESC
//        """)
//    List<Tuple> getPaymentMethodAnalysis(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
//


//    /**
//     * Period over period comparison
//     */
//    @Query(value = """
//        SELECT
//            'CURRENT_PERIOD' as period_type,
//            SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty * item.unitFact ELSE 0 END) as sales_amount,
//            SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty * item.unitFact ELSE 0 END) as purchase_amount,
//            COUNT(CASE WHEN ty.type = 'sale' THEN ih.id END) as sales_count,
//            :startDate as period_start,
//            :endDate as period_end
//        FROM InvoiceHeader ih
//        JOIN ih.invoiceItems item
//        JOIN ih.invoiceType ty
//        WHERE ih.date BETWEEN :startDate AND :endDate
//        AND ih.isPosted = true AND ih.isSuspended = false
//
//        UNION ALL
//
//        SELECT
//            'PREVIOUS_PERIOD' as period_type,
//            SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty * item.unitFact ELSE 0 END) as sales_amount,
//            SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty * item.unitFact ELSE 0 END) as purchase_amount,
//            COUNT(CASE WHEN ty.type = 'sale' THEN ih.id END) as sales_count,
//            DATE_SUB(:startDate, INTERVAL DATEDIFF(:endDate, :startDate) DAY) as period_start,
//            DATE_SUB(:startDate, INTERVAL 1 DAY) as period_end
//        FROM InvoiceHeader ih
//        JOIN ih.invoiceItems item
//        JOIN ih.invoiceType ty
//        WHERE ih.date BETWEEN DATE_SUB(:startDate, INTERVAL DATEDIFF(:endDate, :startDate) DAY) AND DATE_SUB(:startDate, INTERVAL 1 DAY)
//        AND ih.isPosted = true AND ih.isSuspended = false
//        """)
//    List<Tuple> getPeriodComparison(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}

