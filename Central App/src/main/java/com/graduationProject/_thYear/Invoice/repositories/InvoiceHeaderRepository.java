
package com.graduationProject._thYear.Invoice.repositories;

import com.graduationProject._thYear.Invoice.models.InvoiceHeader;

import com.graduationProject._thYear.Invoice.models.InvoiceKind;
import com.graduationProject._thYear.InvoiceType.models.Type;
import jakarta.persistence.Tuple;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvoiceHeaderRepository extends JpaRepository<InvoiceHeader,Integer> {
    boolean existsByIdAndIsPosted(Integer id, Boolean isPosted);

    @Query(value="SELECT MAX(CASE WHEN ty.type IN ('buy') THEN item.price * ih.currencyValue END) as max_buy, " +
            "MIN(CASE WHEN ty.type IN ('buy') THEN item.price * ih.currencyValue  END) as min_buy, " +
            "SUM(CASE WHEN ty.type IN ('buy') THEN item.price * ih.currencyValue * item.qty * item.unitFact END) / SUM(CASE WHEN ty.type IN ('buy') THEN  item.qty * item.unitFact END)  as avg_buy, " +
            "MAX(CASE WHEN ty.type IN ('sale') THEN item.price * ih.currencyValue END) as max_sell, " +
            "MIN(CASE WHEN ty.type IN ('sale') THEN item.price * ih.currencyValue END) as min_sell, " +
            "SUM(CASE WHEN ty.type IN ('sale') THEN item.price * ih.currencyValue * item.qty * item.unitFact END) / SUM(CASE WHEN ty.type IN ('sale') THEN  item.qty * item.unitFact END) as avg_sell, " +
            "item.product.id as product_id, " +
            "item.product.name as product_name, " +
            "item.product.defaultUnit.id as unit_id, " +
            "item.product.defaultUnit.name as unit_name, " +
            ":startDate as start_date, " +
            ":endDate as end_date " +
            "FROM InvoiceHeader ih " +
            "JOIN ih.invoiceItems item " +
            "JOIN ih.invoiceType ty " +
            "WHERE ih.date BETWEEN :startDate AND :endDate " +
            "AND (:productId IS NULL OR item.product.id = (:productId)) " +
            "AND (:groupId IS NULL OR item.product.groupId.id = (:groupId)) " +
            "AND (:warehouseId IS NULL OR ih.warehouse.id = (:warehouseId)) " +
            "AND ih.isPosted = true AND ih.isSuspended = false " +
            "GROUP BY item.product "
    )
    List<Tuple> getMaterialMovementHeader(LocalDateTime startDate, LocalDateTime endDate,Integer productId, Integer groupId, Integer warehouseId);


    @Query(value="SELECT  ih.id invoice_header_id, ty.name invoice_name, item.qty * item.unitFact as quantity, item.price * ih.currencyValue as price, ih.warehouse.id warehouse_id, CASE WHEN ty.type IN ('buy','retrieve_sale','input') THEN 'INBOUND' ELSE 'OUTBOUND' END type, ih.date as date " +
            "FROM InvoiceHeader ih " +
            "JOIN ih.invoiceItems item " +
            "JOIN ih.invoiceType ty " +
            "WHERE ih.date BETWEEN :startDate AND :endDate " +
            "AND (:warehouseId IS NULL OR ih.warehouse.id = (:warehouseId)) " +
            "AND  item.product.id = :productId " +
            "AND ih.isPosted = true AND ih.isSuspended = false "
    )
    List<Tuple> getMaterialMovementItems(LocalDateTime startDate, LocalDateTime endDate,Integer productId, Integer warehouseId);


    @Query(value="SELECT  ih.id invoice_header_id, ty.name invoice_name, item.qty * item.unitFact quantity, " +
            "item.price * ih.currencyValue individual_price, " +
            "item.price * ih.currencyValue * item.qty * item.unitFact as total_price, " +
            "ih.warehouse.id warehouse_id, ih.date as date," +
            "item.unitItem.id as unit_id, item.unitItem.name unit_name, " +
            "item.product.id as product_id, item.product.name product_name " +
            "FROM InvoiceHeader ih " +
            "JOIN ih.invoiceItems item " +
            "JOIN ih.invoiceType ty " +
            "WHERE ih.date BETWEEN :startDate AND :endDate " +
            "AND ih.isPosted = true AND ih.isSuspended = false " +
            "AND (:productId IS NULL OR item.product.id = (:productId)) " +
            "AND (:groupId IS NULL OR item.product.groupId.id = (:groupId)) " +
            "AND (:warehouseId IS NULL OR ih.warehouse.id = (:warehouseId))")
    List<Tuple> getDailyMovementMainItems(LocalDateTime startDate, LocalDateTime endDate,Integer productId, Integer groupId, Integer warehouseId);

    @Query(value="SELECT COALESCE(SUM(CASE WHEN ih.payType = 0 THEN (item.price * ih.currencyValue * item.qty * item.unitFact) END),0) as cash_total, " +
            "COALESCE(SUM(CASE WHEN ih.payType = 1 THEN item.price * ih.currencyValue * item.qty * item.unitFact END),0) as future_total, ty.name as invoice_name " +
            "FROM InvoiceHeader ih " +
            "JOIN ih.invoiceItems item " +
            "JOIN ih.invoiceType ty " +
            "WHERE ih.date BETWEEN :startDate AND :endDate " +
            "AND ih.isPosted = true AND ih.isSuspended = false " +
            "AND (:productId IS NULL OR item.product.id = (:productId)) " +
            "AND (:groupId IS NULL OR item.product.groupId.id = (:groupId)) " +
            "AND (:warehouseId IS NULL OR ih.warehouse.id = (:warehouseId)) " +
            "GROUP BY ty "
    )
    List<Tuple> getDailyMovementSideItems(LocalDateTime startDate, LocalDateTime endDate,Integer productId, Integer groupId, Integer warehouseId);



    @Query(value="SELECT  item.product.id as product_id, item.product.name as product_name, ih.warehouse.id as warehouse_id, item.unitItem.id as unit_id, item.unitItem.name unit_name, " +
            "SUM(CASE WHEN ty.type IN ('buy','retrieve_sale','input') THEN item.qty * item.unitFact ELSE 0 END) - SUM(CASE WHEN ty.type IN ('sale','retrieve_buy','ouput') THEN item.qty * item.unitFact ELSE 0 END)  as total_quantity, " +
            "SUM(CASE WHEN ty.type IN ('buy','retrieve_sale','input') THEN item.qty * item.unitFact * item.price * ih.currencyValue ELSE 0 END) - SUM(CASE WHEN ty.type IN ('sale','retrieve_buy','ouput') THEN item.qty * item.unitFact * item.price * ih.currencyValue ELSE 0 END) as total_price " +
            "FROM InvoiceHeader ih " +
            "JOIN ih.invoiceItems item " +
            "JOIN ih.invoiceType ty " +
            "WHERE ih.date BETWEEN :startDate AND :endDate " +
            "AND ih.isPosted = true AND ih.isSuspended = false " +
            "AND (:productId IS NULL OR item.product.id = (:productId)) " +
            "AND (:groupId IS NULL OR item.product.groupId.id = (:groupId)) " +
            "AND (:warehouseId IS NULL OR ih.warehouse.id = (:warehouseId)) " +
            "GROUP BY item.product "
    )
    List<Tuple> getProductStockMainItems(LocalDateTime startDate, LocalDateTime endDate,Integer productId, Integer groupId, Integer warehouseId);

    @Query(value="SELECT  SUM(CASE WHEN total_quantity > 0 THEN total_quantity ELSE 0 END)  as total_quantity_positive, " +
            "SUM(CASE WHEN total_quantity < 0 THEN total_quantity ELSE 0 END)  as total_quantity_negative, " +
            "SUM(CASE WHEN total_price > 0 THEN total_price ELSE 0 END)  as total_price_positive, " +
            "SUM(CASE WHEN total_price < 0 THEN total_price ELSE 0 END)  as total_price_negative, " +
            "COALESCE(SUM(total_price),0)  as total_price, " +
            "COALESCE(SUM(total_quantity),0)  as total_quantity " +
            "FROM (" + "SELECT  item.product.id as product_id, item.product.name as product_name, ih.warehouse.id as warehouse_id, item.unitItem.id as unit_id, item.unitItem.name unit_name, " +
            "SUM(CASE WHEN ty.type IN ('buy','retrieve_sale','input') THEN item.qty ELSE 0 END) - SUM(CASE WHEN ty.type IN ('sale','retrieve_buy','ouput') THEN item.qty ELSE 0 END)  as total_quantity, " +
            "SUM(CASE WHEN ty.type IN ('buy','retrieve_sale','input') THEN item.qty * item.price ELSE 0 END) - SUM(CASE WHEN ty.type IN ('sale','retrieve_buy','ouput') THEN item.qty * item.price ELSE 0 END) as total_price " +
            "FROM InvoiceHeader ih " +
            "JOIN ih.invoiceItems item " +
            "JOIN ih.invoiceType ty " +
            "WHERE ih.date BETWEEN :startDate AND :endDate " +
            "AND ih.isPosted = true AND ih.isSuspended = false " +
            "AND (:productId IS NULL OR item.product.id = (:productId)) " +
            "AND (:groupId IS NULL OR item.product.groupId.id = (:groupId)) " +
            "AND (:warehouseId IS NULL OR ih.warehouse.id = (:warehouseId)) " +
            "GROUP BY item.product " +
            ")"
    )
    Tuple getProductStockSideItems(LocalDateTime startDate, LocalDateTime endDate,Integer productId, Integer groupId, Integer warehouseId);

   @Query(value = """
                SELECT ih FROM InvoiceHeader ih
                WHERE ih.createdAt > :date
           """)
    List<InvoiceHeader> findCreatedAfterDateTime(LocalDateTime date);

    @Query(value = """
                SELECT ih FROM InvoiceHeader ih
                WHERE ih.updatedAt > :date
           """)
    List<InvoiceHeader> findUpdatedAfterDateTime(LocalDateTime date);


    @Query(value = """
                SELECT ih FROM InvoiceHeader ih
                WHERE ih.deletedAt > :date
           """)
    List<InvoiceHeader> findDeletedAfterDateTime(LocalDateTime date);


    List<InvoiceHeader> findByInvoiceType_Type(Type type);

    List<InvoiceHeader> findByInvoiceType_Id(Integer id);

    List<InvoiceHeader> findByParentTypeAndParentId(InvoiceKind parentType, Integer parentId);

    /**
     * Total sales for different time periods with comparison to previous periods
     */
    @Query(value = """
        SELECT
            SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as total_sales,
            COUNT(CASE WHEN ty.type = 'sale' THEN ih.id END) as total_invoices,
            :startDate as period_start,
            :endDate as period_end
        FROM InvoiceHeader ih
        JOIN ih.invoiceItems item
        JOIN ih.invoiceType ty
        WHERE ih.date BETWEEN :startDate AND :endDate
        AND ih.isPosted = true AND ih.isSuspended = false
        """)
    Tuple getTotalSales(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);



    /**
     * Total purchases for different time periods
     */
    @Query(value = """
        SELECT
            SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as total_purchases,
            COUNT(CASE WHEN ty.type = 'buy' THEN ih.id END) as total_purchase_invoices,
            :startDate as period_start,
            :endDate as period_end
        FROM InvoiceHeader ih
        JOIN ih.invoiceItems item
        JOIN ih.invoiceType ty
        WHERE ih.date BETWEEN :startDate AND :endDate
        AND ih.isPosted = true AND ih.isSuspended = false
        """)
    Tuple getTotalPurchases(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * System overview counts (branches, warehouses, POS, active employees)
     */
    @Query(value = """
        SELECT
            (SELECT COUNT(*) FROM Branch) as total_branches,
            (SELECT COUNT(*) FROM Warehouse WHERE type = 'WAREHOUSE') as total_warehouses,
            (SELECT COUNT(*) FROM Warehouse WHERE type = 'POS') as total_pos,
            (SELECT COUNT(*) FROM User WHERE warehouse.id IS NOT NULL) as total_active_employees
        """)
    Tuple getSystemOverview();


    /**
     * Total products and stock overview
     */
    @Query(value = """
        SELECT
            (SELECT COUNT(*) FROM Product) as total_products,
            (SELECT COUNT(*) FROM Product WHERE type = 0) as warehouse_products,
            (SELECT COUNT(*) FROM Product WHERE type = 1) as service_products,
            (SELECT COALESCE(SUM(ps.quantity), 0) FROM ProductStock ps) as total_stock_quantity
        """)
    Tuple getProductOverview();

    /**
     * Branch sales performance with share of total
     */
    @Query(value = """
        SELECT
            b.id as branch_id,
            b.name as branch_name,
            SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currency_value * item.qty  ELSE 0 END) as branch_sales,
            COUNT(CASE WHEN ty.type = 'sale' THEN ih.id END) as branch_invoice_count
        FROM branch b
        JOIN warehouse w ON w.branch_id = b.id
        JOIN invoice_header ih ON ih.warehouse_id = w.id
        JOIN invoice_item item ON item.invoice_header_id = ih.id
        JOIN invoice_type ty ON ty.id = ih.invoice_type_id
        WHERE ih.date BETWEEN :startDate AND :endDate
          AND ih.is_posted = true AND ih.is_suspended = false
        GROUP BY b.id, b.name
        ORDER BY branch_sales DESC
        """, nativeQuery = true)
    List<Tuple> getBranchSalesPerformance(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Top-selling products by branch
     */
    @Query(value = """
        SELECT *
        FROM (
            SELECT
                b.id as branch_id,
                b.name as branch_name,
                p.id as product_id,
                p.name as product_name,
                SUM(CASE WHEN ty.type = 'sale' THEN item.qty * item.unit_fact ELSE 0 END) as total_quantity_sold,
                SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currency_value * item.qty  ELSE 0 END) as total_sales_value,
                ROW_NUMBER() OVER (
                    PARTITION BY b.id
                    ORDER BY SUM(CASE WHEN ty.type = 'sale' THEN item.qty * item.unit_fact ELSE 0 END) DESC
                ) as rank_in_branch
            FROM branch b
            JOIN warehouse w ON w.branch_id = b.id
            JOIN invoice_header ih ON ih.warehouse_id = w.id
            JOIN invoice_item item ON item.invoice_header_id = ih.id
            JOIN invoice_type ty ON ty.id = ih.invoice_type_id
            JOIN product p ON p.id = item.product_id
            WHERE ih.date BETWEEN :startDate AND :endDate
              AND ih.is_posted = true
              AND ih.is_suspended = false
              AND ty.type = 'sale'
            GROUP BY b.id, b.name, p.id, p.name
        ) ranked
        WHERE ranked.rank_in_branch <= 5
        ORDER BY ranked.branch_id, ranked.rank_in_branch
        """, nativeQuery = true)
    List<Tuple> getTopSellingProductsByBranch(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    /**
     * Most returned or slow-moving products by branch
     */
    @Query(value = """
        SELECT *
        FROM (
            SELECT
                b.id as branch_id,
                b.name as branch_name,
                p.id as product_id,
                p.name as product_name,
                SUM(CASE WHEN ty.type = 'retrieve_sale' THEN item.qty * item.unit_fact ELSE 0 END) as total_returns,
                SUM(CASE WHEN ty.type = 'retrieve_sale' THEN item.price * ih.currency_value * item.qty  ELSE 0 END) as total_returns_value,
                ROW_NUMBER() OVER (
                    PARTITION BY b.id
                    ORDER BY SUM(CASE WHEN ty.type = 'retrieve_sale' THEN item.qty * item.unit_fact ELSE 0 END) DESC
                ) as return_rank
            FROM branch b
            JOIN warehouse w ON w.branch_id = b.id
            JOIN invoice_header ih ON ih.warehouse_id = w.id
            JOIN invoice_item item ON item.invoice_header_id = ih.id
            JOIN invoice_type ty ON ty.id = ih.invoice_type_id
            JOIN product p ON p.id = item.product_id
            WHERE ih.date BETWEEN :startDate AND :endDate
              AND ih.is_posted = true
              AND ih.is_suspended = false
              AND ty.type = 'retrieve_sale'
            GROUP BY b.id, b.name, p.id, p.name
        ) ranked
        WHERE ranked.return_rank <= 5
        ORDER BY ranked.branch_id, ranked.return_rank
        """, nativeQuery = true)
    List<Tuple> getMostReturnedProductsByBranch(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

    /**
     * Employee/cashier performance by branch
     */
    @Query(value = """
            SELECT
                b.id as branch_id,
                b.name as branch_name,
                u.id as user_id,
                CONCAT(u.firstName, ' ', u.lastName) as employee_name,
                u.role as employee_role,
                COUNT(CASE WHEN ty.type = 'sale' THEN ih.id END) as invoices_processed,
                SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty * item.unitFact ELSE 0 END) as total_sales_amount,
                SUM(CASE WHEN ty.type = 'sale' THEN item.qty * item.unitFact ELSE 0 END) as total_quantity_sold,
                :startDate as period_start,
                :endDate as period_end
            FROM Branch b
            JOIN Warehouse w ON w.branch.id = b.id
            JOIN User u ON u.warehouse.id = w.id
            LEFT JOIN InvoiceHeader ih ON ih.user.id = u.id
            LEFT JOIN ih.invoiceItems item ON item.invoiceHeader.id = ih.id
            LEFT JOIN ih.invoiceType ty ON ty.id = ih.invoiceType.id
            WHERE (ih.date BETWEEN :startDate AND :endDate OR ih.date IS NULL)
            AND ih.isPosted = true AND ih.isSuspended = false
            GROUP BY b.id, b.name, u.id, u.firstName, u.lastName, u.role
            ORDER BY b.id, total_sales_amount DESC
            """)
    List<Tuple> getEmployeePerformanceByBranch(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    /**
     * Stock levels analysis by branch (Low/Sufficient/Overstocked)
     */
    @Query(value = """
        SELECT
            b.id as branch_id,
            b.name as branch_name,
            p.id as product_id,
            p.name as product_name,
            p.minQty as min_quantity,
            p.maxQty as max_quantity,
            COALESCE(ps.quantity, 0) as current_stock,
            CASE
                WHEN COALESCE(ps.quantity, 0) <= p.minQty THEN 'LOW'
                WHEN COALESCE(ps.quantity, 0) >= p.maxQty THEN 'OVERSTOCKED'
                ELSE 'SUFFICIENT'
            END as stock_status
        FROM Branch b
        JOIN Warehouse w ON w.branch.id = b.id
        JOIN Product p ON p.type = 0
        LEFT JOIN ProductStock ps ON ps.product.id = p.id AND ps.warehouse.id = w.id
        WHERE w.type = 'WAREHOUSE'
        ORDER BY b.id, stock_status, current_stock
        """)
    List<Tuple> getStockLevelsByBranch();



    // ==================== PRODUCT ANALYTICS ====================

    /**
     * Product performance analysis (profitable vs unprofitable)
     */
    @Query(value = """
        SELECT
            p.id as product_id,
            p.name as product_name,
            p.groupId.name as product_group,
            SUM(CASE WHEN ty.type = 'sale' THEN item.qty * item.unitFact ELSE 0 END) as total_quantity_sold,
            SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as total_sales_revenue,
            SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as total_purchase_cost,
            (SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) -
             SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END)) as gross_profit,
            CASE
                WHEN SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) > 0
                THEN ((SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) -
                       SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END)) /
                       SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END)) * 100
                ELSE 0
            END as profit_margin_percentage,
            :startDate as period_start,
            :endDate as period_end
        FROM Product p
        LEFT JOIN p.groupId
        LEFT JOIN InvoiceHeader ih ON ih.date BETWEEN :startDate AND :endDate
        LEFT JOIN ih.invoiceItems item ON item.product.id = p.id
        LEFT JOIN ih.invoiceType ty ON ty.id = ih.invoiceType.id
        WHERE (ih.isPosted = true AND ih.isSuspended = false)
        GROUP BY p.id, p.name, p.groupId.name
        HAVING SUM(CASE WHEN ty.type = 'sale' THEN item.qty * item.unitFact ELSE 0 END) > 0
        ORDER BY gross_profit DESC
        """)
    List<Tuple> getProductProfitabilityAnalysis(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    /**
     * Sales trend analysis over time (daily, weekly, monthly)
     */
    @Query(value = """
        SELECT
            DATE(ih.date) as sale_date,
            SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currency_value * item.qty  ELSE 0 END) as daily_sales,
            SUM(CASE WHEN ty.type = 'sale' THEN item.qty  ELSE 0 END) as daily_quantity,
            COUNT(CASE WHEN ty.type = 'sale' THEN ih.id END) as daily_invoices
        FROM invoice_header ih
        JOIN invoice_item item ON item.invoice_header_id = ih.id
        JOIN invoice_type ty ON ty.id = ih.invoice_type_id
        WHERE ih.date BETWEEN :startDate AND :endDate
          AND ih.is_posted = true AND ih.is_suspended = false
          AND ty.type = 'sale'
        GROUP BY DATE(ih.date)
        ORDER BY sale_date
        """, nativeQuery = true)
    List<Tuple> getSalesTrendAnalysis(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Monthly comparison for trend analysis
     */
    @Query(value = """
        SELECT
            t.sale_year as `sale_year`,
            t.sale_month as `sale_month`,
            CONCAT(t.sale_year, '-', LPAD(t.sale_month, 2, '0')) as `year_month`,
            SUM(t.sales_value) as `monthly_sales`,
            SUM(t.sales_qty) as `monthly_quantity`,
            SUM(t.sales_invoices) as `monthly_invoices`
        FROM (
            SELECT
                YEAR(ih.date) as sale_year,
                MONTH(ih.date) as sale_month,
                CASE WHEN ty.type = 'sale' THEN (item.price * ih.currency_value * item.qty) ELSE 0 END as sales_value,
                CASE WHEN ty.type = 'sale' THEN item.qty ELSE 0 END as sales_qty,
                CASE WHEN ty.type = 'sale' THEN 1 ELSE 0 END as sales_invoices
            FROM invoice_header ih
            JOIN invoice_item item ON item.invoice_header_id = ih.id
            JOIN invoice_type ty ON ty.id = ih.invoice_type_id
            WHERE ih.date BETWEEN :startDate AND :endDate
              AND ih.is_posted = true AND ih.is_suspended = false
        ) t
        GROUP BY t.sale_year, t.sale_month
        ORDER BY `sale_year`, `sale_month`
        """, nativeQuery = true)
    List<Tuple> getMonthlySalesComparison(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // ==================== FINANCIAL ANALYTICS ====================

    /**
     * Revenue vs Cost analysis
     */
    @Query(value = """
        SELECT
            SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as total_revenue,
            SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as total_cost,
            SUM(CASE WHEN ty.type IN ('retrieve_buy', 'retrieve_sale') THEN item.price * ih.currencyValue * item.qty  ELSE 0 END) as total_returns_value,
            (SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty ELSE 0 END) -
             SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END)) as gross_profit,
            CASE
                WHEN SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty ELSE 0 END) > 0
                THEN ((SUM(CASE WHEN ty.type = 'sale' THEN item.price * ih.currencyValue * item.qty ELSE 0 END) -
                       SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END)) /
                       SUM(CASE WHEN ty.type = 'buy' THEN item.price * ih.currencyValue * item.qty  ELSE 0 END)) * 100
                ELSE 0
            END as profit_margin_percentage,
            :startDate as period_start,
            :endDate as period_end
        FROM InvoiceHeader ih
        JOIN ih.invoiceItems item
        JOIN ih.invoiceType ty
        WHERE ih.date BETWEEN :startDate AND :endDate
        AND ih.isPosted = true AND ih.isSuspended = false
        """)
    Tuple getFinancialAnalysis(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


}
