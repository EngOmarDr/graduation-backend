package com.graduationProject._thYear.Invoice.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class DailyMovementResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private String currency;

    @Default
    private List<DailyMovemntMainItems> mainItems= new LinkedList<>();
    @Default
    private List<DailyMovemntSideItems> sideItems = new LinkedList<>();

    @AllArgsConstructor
    @Data
    @SuperBuilder
    public static class DailyMovemntMainItems{
        private Integer invoiceItemId;
        private String invoiceName;
        private Integer warehouseId;
        private BigDecimal quantity;
        private BigDecimal individualPrice;
        private BigDecimal totalPrice;
        private LocalDate date;

        public static DailyMovemntMainItems fromTuple(Tuple tuple){
            return DailyMovemntMainItems.builder()
                .invoiceItemId((Integer) tuple.get("invoice_item_id"))
                .invoiceName((String) tuple.get("invoice_name"))
                .warehouseId((Integer) tuple.get("warehouse_id"))
                .quantity((BigDecimal) tuple.get("quantity"))
                .individualPrice((BigDecimal) tuple.get("individual_price"))
                .totalPrice((BigDecimal) tuple.get("total_price"))
                .date(((LocalDateTime) tuple.get("date")).toLocalDate())
                .build();
        }
    }

    @AllArgsConstructor
    @Data
    @SuperBuilder
    public static class DailyMovemntSideItems{
        private BigDecimal cashTotal;
        private Long futureTotal;
        private String invoiceName;

        public static DailyMovemntSideItems fromTuple(Tuple tuple){
            return DailyMovemntSideItems.builder()
                .cashTotal((BigDecimal) tuple.get("cash_total"))
                .futureTotal((Long) tuple.get("future_total"))
                .invoiceName((String) tuple.get("invoice_name"))
                .build();
        }
    }
}
