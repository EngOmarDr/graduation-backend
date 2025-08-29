package com.graduationProject._thYear.EventSyncronization.Records;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.graduationProject._thYear.Group.models.Group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRecord {
    private Integer id;
    private UUID globalId;
    private String code;
    private String name;
    private GroupRecord group;
    private String image;
    private Byte type;
    private String typeName;
    private Integer defaultUnitId;
    private Float minQty;
    private Float maxQty;
    private Float orderQty;
    private String notes;
    private List<ProdcutPriceRecord> prices;
    private List<ProductBarcodRecord> barcodes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class GroupRecord{
        private Integer id;
        private UUID globalId;
        private String code;
        private String name;
        private String notes;
        private GroupRecord parent;

        static GroupRecord fromGroupEntity(Group group){
            if (group == null){
                return null;
            }
            return GroupRecord.builder()

                .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class UnitRecord{
        private Integer id;
        private UUID globalId;
        private String name;
        private List<UnitItemRecord> unitItems;
        
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class ProdcutPriceRecord{
        private Integer id;
        private UUID globalId;
        private PriceRecord priceRecord;
        private UnitItemRecord unitItemRecord;
        private BigDecimal price;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        static class PriceRecord{
            private Integer id;
            private UUID globalId;
            private String name;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class ProductBarcodRecord{
        private Integer id;
        private UUID globalId;
        private UnitItemRecord unitItemRecord;
        private String barcode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class UnitItemRecord {
        private Integer id;
        private UUID globalId;  
        private Integer unitId;
        private String unitName;
        private String name;
        private Float fact;
        private Boolean isDef;
    }


}
