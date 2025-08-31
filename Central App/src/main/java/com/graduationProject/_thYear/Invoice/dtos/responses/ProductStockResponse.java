package com.graduationProject._thYear.Invoice.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Data
@SuperBuilder
public class ProductStockResponse {
     private LocalDate startDate;
    private LocalDate endDate;
    private String currencyName;
    private String currencyCode;


    @Default
    private List<ProductStockMainItems> mainItems= new LinkedList<>();
    private ProductStockSideItems sideItems;

    @AllArgsConstructor
    @Data
    @SuperBuilder
    public static class ProductStockMainItems{
        private Integer productId;
        private String productName;
        private Integer warehouseId;
        private Integer unitId;
        private String unitName;
        private BigDecimal quantity;
        private BigDecimal totalPrice;

   
        public static ProductStockMainItems fromTuple(Tuple tuple){
            ProductStockMainItems productStockMainItems = ProductStockMainItems.builder()
                .productId((Integer) tuple.get("product_id"))
                .productName((String) tuple.get("product_name"))
                .warehouseId((Integer) tuple.get("warehouse_id"))
                .quantity((BigDecimal) tuple.get("total_quantity"))
                .totalPrice((BigDecimal) tuple.get("total_price"))
                .unitId((Integer) tuple.get("unit_id"))
                .unitName((String) tuple.get("unit_name"))
                .build();
            return productStockMainItems;
        }
    }

    @AllArgsConstructor
    @Data
    @SuperBuilder
    public static class ProductStockSideItems{
        private BigDecimal totalPrice;
        private BigDecimal toatlPriceNegative;
        private BigDecimal toatlPricePositive;
        private Double totalQuantity;
        private Double totalQuantityPositive;
        private Double totalQuantityNegative;


        public static ProductStockSideItems fromTuple(Tuple tuple){
            return ProductStockSideItems.builder()
                .totalPrice((BigDecimal) tuple.get("total_price"))
                .toatlPricePositive((BigDecimal) tuple.get("total_price_positive"))
                .toatlPriceNegative((BigDecimal) tuple.get("total_price_negative"))
                .totalQuantity((Double) tuple.get("total_quantity"))
                .totalQuantityPositive((Double) tuple.get("total_quantity_positive"))
                .totalQuantityNegative((Double) tuple.get("total_quantity_negative"))
                .build();

        }
    }
}
