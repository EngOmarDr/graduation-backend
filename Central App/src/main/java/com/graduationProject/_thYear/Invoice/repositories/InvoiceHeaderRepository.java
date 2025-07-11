package com.graduationProject._thYear.Invoice.repositories;

import com.graduationProject._thYear.Invoice.models.InvoiceHeader;

import jakarta.persistence.Tuple;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvoiceHeaderRepository extends JpaRepository<InvoiceHeader,Integer> {
    boolean existsByIdAndIsPosted(Integer id, Boolean isPosted);

    @Query(value="SELECT MAX(CASE WHEN ty.type IN (1,3,5) THEN item.price ELSE 0 END) as max_sell, " + 
        "MIN(CASE WHEN ty.type IN (1,3,5) THEN item.price ELSE 0 END) as min_sell, " + 
        "AVG(CASE WHEN ty.type IN (1,3,5) THEN item.price ELSE 0 END) as avg_sell, " + 
        "MAX(CASE WHEN ty.type IN (2,4,6) THEN item.price ELSE 0 END) as max_purchase, " + 
        "MIN(CASE WHEN ty.type IN (2,4,6) THEN item.price ELSE 0 END) as min_purchase, " + 
        "AVG(CASE WHEN ty.type IN (2,4,6) THEN item.price ELSE 0 END) as avg_purchase, " + 
        "item.product.id as product_id, " + 
        "item.product.name as product_name, " + 
        ":startDate as start_date, " + 
        ":endDate as end_date " + 
        "FROM InvoiceHeader ih " +
        "JOIN ih.invoiceItems item " +
        "JOIN ih.invoiceType ty " +
        "WHERE ih.date BETWEEN :startDate AND :endDate " +
        "AND (:productId IS NULL OR item.product.id = (:productId)) " +
        "AND (:groupId IS NULL OR item.product.groupId.id = (:groupId)) " +
        "AND (:warehouseId IS NULL OR ih.warehouse.id = (:warehouseId)) " +
        "GROUP BY item.product " 
        )
    List<Tuple> getMaterialMovementHeaderBetweenTwoDates(LocalDateTime startDate, LocalDateTime endDate,Integer productId, Integer groupId, Integer warehouseId);


    @Query(value="SELECT  item.id invoice_item_id, ty.name invoice_name, item.qty quantity, item.price price, ih.warehouse.id warehouse_id, CASE WHEN ty.type IN (1,3,5) THEN 'INBOUND' ELSE 'OUTBOUND' END type " + 
        "FROM InvoiceHeader ih " +
        "JOIN ih.invoiceItems item " +
        "JOIN ih.invoiceType ty " +
        "WHERE ih.date BETWEEN :startDate AND :endDate " +
        "AND (:warehouseId IS NULL OR ih.warehouse.id = (:warehouseId)) " +
        "AND  item.product.id = :productId")
    List<Tuple> getMaterialMovementItemsBetweenTwoDates(LocalDateTime startDate, LocalDateTime endDate,Integer productId, Integer warehouseId);

}
