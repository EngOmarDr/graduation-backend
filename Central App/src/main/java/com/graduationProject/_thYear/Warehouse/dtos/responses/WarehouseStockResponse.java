package com.graduationProject._thYear.Warehouse.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WarehouseStockResponse {
    
    private Integer warehouseId;
    private String warehouseName;
    private List<WarehouseStockItem> items;


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class  WarehouseStockItem {
        private Integer productId;
        private String productName;
        private BigDecimal quantity;


        public static WarehouseStockItem fromTuple(Tuple tuple){
            return WarehouseStockItem.builder()
                .productId((Integer)tuple.get("product_id"))
                .productName((String)tuple.get("product_name"))
                .quantity((BigDecimal)tuple.get("quantity"))
                .build();
        }
        
    }

}
