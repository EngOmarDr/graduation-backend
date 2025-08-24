//package com.graduationProject._thYear.Statistics.repositories;
//
//
//import jakarta.persistence.Tuple;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Repository
//public interface StatisticsRepository extends JpaRepository<Object,Integer> {
//
//
////
////    // ==================== WAREHOUSE AND INVENTORY ANALYTICS ====================
////
////    /**
////     * Warehouse performance comparison
////     */
////    @Query(value = """
////        SELECT
////            w.id as warehouse_id,
////            w.name as warehouse_name,
////            w.type as warehouse_type,
////            b.name as branch_name,
////            SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty * item.unitFact ELSE 0 END) as total_sales,
////            SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty * item.unitFact ELSE 0 END) as total_purchases,
////            SUM(CASE WHEN ty.type IN ('retrieve_buy', 'retrieve_sale') THEN item.price * ih.currencyValue * item.qty * item.unitFact ELSE 0 END) as total_returns,
////            COUNT(CASE WHEN ty.type = 'sale' THEN ih.id END) as sales_invoices,
////            COUNT(CASE WHEN ty.type = 'buy' THEN ih.id END) as purchase_invoices,
////            :startDate as period_start,
////            :endDate as period_end
////        FROM Warehouse w
////        JOIN Branch b ON b.id = w.branch_id
////        LEFT JOIN InvoiceHeader ih ON ih.warehouse_id = w.id
////        LEFT JOIN ih.invoiceItems item ON item.invoiceHeader_id = ih.id
////        LEFT JOIN ih.invoiceType ty ON ty.id = ih.invoiceType_id
////        WHERE (ih.date BETWEEN :startDate AND :endDate OR ih.date IS NULL)
////        AND (ih.isPosted = true OR ih.isPosted IS NULL)
////        AND (ih.isSuspended = false OR ih.isSuspended IS NULL)
////        GROUP BY w.id, w.name, w.type, b.name
////        ORDER BY total_sales DESC
////        """)
////    List<Tuple> getWarehousePerformanceComparison(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
////
////    /**
////     * Inventory turnover analysis
////     */
////    @Query(value = """
////        SELECT
////            p.id as product_id,
////            p.name as product_name,
////            p.groupId.name as product_group,
////            COALESCE(ps.quantity, 0) as current_stock,
////            SUM(CASE WHEN ty.type = 'sale' THEN item.qty * item.unitFact ELSE 0 END) as total_sold_period,
////            CASE
////                WHEN COALESCE(ps.quantity, 0) > 0
////                THEN SUM(CASE WHEN ty.type = 'sale' THEN item.qty * item.unitFact ELSE 0 END) / COALESCE(ps.quantity, 1)
////                ELSE 0
////            END as turnover_ratio,
////            CASE
////                WHEN COALESCE(ps.quantity, 0) > 0 AND SUM(CASE WHEN ty.type = 'sale' THEN item.qty * item.unitFact ELSE 0 END) > 0
////                THEN (COALESCE(ps.quantity, 0) / SUM(CASE WHEN ty.type = 'sale' THEN item.qty * item.unitFact ELSE 0 END)) * 30
////                ELSE 999
////            END as days_of_inventory,
////            :startDate as period_start,
////            :endDate as period_end
////        FROM Product p
////        LEFT JOIN p.groupId
////        LEFT JOIN ProductStock ps ON ps.product_id = p.id
////        LEFT JOIN InvoiceHeader ih ON ih.date BETWEEN :startDate AND :endDate
////        LEFT JOIN ih.invoiceItems item ON item.product_id = p.id
////        LEFT JOIN ih.invoiceType ty ON ty.id = ih.invoiceType_id
////        WHERE (ih.isPosted = true OR ih.isPosted IS NULL)
////        AND (ih.isSuspended = false OR ih.isSuspended IS NULL)
////        AND p.type = 0
////        GROUP BY p.id, p.name, p.groupId.name, ps.quantity
////        HAVING total_sold_period > 0
////        ORDER BY turnover_ratio DESC
////        """)
////    List<Tuple> getInventoryTurnoverAnalysis(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
//
////    // ==================== FINANCIAL ANALYTICS ====================
////
////    /**
////     * Revenue vs Cost analysis
////     */
////    @Query(value = """
////        SELECT
////            SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as total_revenue,
////            SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as total_cost,
////            SUM(CASE WHEN ty.type IN ('retrieve_buy', 'retrieve_sale') THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as total_returns_value,
////            (SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty ELSE 0 END) -
////             SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END)) as gross_profit,
////            CASE
////                WHEN SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty ELSE 0 END) > 0
////                THEN ((SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty ELSE 0 END) -
////                       SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END)) /
////                       SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END)) * 100
////                ELSE 0
////            END as profit_margin_percentage,
////            :startDate as period_start,
////            :endDate as period_end
////        FROM InvoiceHeader ih
////        JOIN ih.invoiceItems item
////        JOIN ih.invoiceType ty
////        WHERE ih.date BETWEEN :startDate AND :endDate
////        AND ih.isPosted = true AND ih.isSuspended = false
////        """)
////    Tuple getFinancialAnalysis(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
//
//
////    // ==================== COMPARATIVE ANALYSIS ====================
//
//}
