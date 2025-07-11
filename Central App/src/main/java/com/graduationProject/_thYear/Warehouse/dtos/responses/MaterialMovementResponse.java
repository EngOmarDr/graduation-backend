package com.graduationProject._thYear.Warehouse.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MaterialMovementResponse {
    private Integer productId;
    private String productName;
    private BigDecimal maxSell;
    private BigDecimal minSell;
    private BigDecimal avgSell;
    private BigDecimal maxPurchase;
    private BigDecimal minPurchase;
    private BigDecimal avgPurchase;
    private LocalDate startDate;
    private LocalDate endDate;

    @Default
    private List<MaterialMovementItem> items = new LinkedList<>();


    public static MaterialMovementResponse fromTuple(Tuple tuple){
        return MaterialMovementResponse.builder()
            .productId((Integer) tuple.get("product_id"))
            .productName((String) tuple.get("product_name"))
            .maxSell((BigDecimal) tuple.get("max_sell"))
            .minSell((BigDecimal) tuple.get("min_sell"))
            .avgSell(BigDecimal.valueOf( (Double)tuple.get("avg_sell")))
            .maxPurchase((BigDecimal) tuple.get("max_purchase"))
            .minPurchase((BigDecimal) tuple.get("min_purchase"))
            .avgPurchase(BigDecimal.valueOf( (Double) tuple.get("avg_purchase")))
            .startDate(((LocalDateTime) tuple.get("start_date")).toLocalDate())
            .endDate(((LocalDateTime) tuple.get("end_date")).toLocalDate())
            .build();
    }

    public void addItem(MaterialMovementItem item){
        items.add(item);
    }

    @AllArgsConstructor
    @Data
    @SuperBuilder
    public static class MaterialMovementItem {
        private Integer warehouseId;
        private Integer invoiceItemId;
        private String invoiceName;
        private BigDecimal price;
        private BigDecimal quantity;
        private String type;

        public static MaterialMovementItem fromTuple(Tuple tuple){
            return MaterialMovementItem.builder()
                .warehouseId((Integer) tuple.get("warehouse_id"))
                .invoiceItemId((Integer) tuple.get("invoice_item_id"))
                .invoiceName((String) tuple.get("invoice_name"))
                .price((BigDecimal) tuple.get("price"))
                .quantity((BigDecimal) tuple.get("quantity"))
                .type((String) tuple.get("type"))
                .build();
        }

        
    }
    
}
