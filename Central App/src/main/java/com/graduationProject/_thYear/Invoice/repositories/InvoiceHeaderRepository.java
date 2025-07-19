package com.graduationProject._thYear.Invoice.repositories;

import com.graduationProject._thYear.Invoice.models.InvoiceHeader;

import com.graduationProject._thYear.InvoiceType.models.Type;
import jakarta.persistence.Tuple;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

   @Query(value="SELECT COALESCE(SUM(item.price * ih.currencyValue * item.qty * item.unitFact) ,0) as cash_total, COALESCE(SUM(0),0) as future_total, ty.name as invoice_name " +
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


    List<InvoiceHeader> findByInvoiceType_Type(Type type);
    
    List<InvoiceHeader> findByInvoiceType_Id(Integer id);

}
