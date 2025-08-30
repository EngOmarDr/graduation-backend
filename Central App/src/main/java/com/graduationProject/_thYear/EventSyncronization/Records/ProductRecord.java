package com.graduationProject._thYear.EventSyncronization.Records;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.graduationProject._thYear.Group.models.Group;
import com.graduationProject._thYear.Product.models.Price;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.models.ProductBarcode;
import com.graduationProject._thYear.Product.models.ProductPrice;
import com.graduationProject._thYear.Unit.models.Unit;
import com.graduationProject._thYear.Unit.models.UnitItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRecord {
    private UUID globalId;
    private String code;
    private String name;
    private GroupRecord group;
    private String image;
    private Byte type;
    private String typeName;
    private UnitRecord defaultUnit;
    private Float minQty;
    private Float maxQty;
    private Float orderQty;
    private String notes;
    private List<ProductPriceRecord> prices;
    private List<ProductBarcodRecord> barcodes;

    public static ProductRecord fromProductEntity(Product product){
        return ProductRecord.builder()
            .globalId(product.getGlobalId())
            .code(product.getCode())
            .name(product.getName())
            .group(GroupRecord.fromGroupEntity(product.getGroupId()))
            .image(product.getImage())
            .type(product.getType())
            .defaultUnit(UnitRecord.fromUnitEntity(product.getDefaultUnit()))
            .minQty(product.getMinQty())
            .maxQty(product.getMaxQty())
            .orderQty(product.getOrderQty())
            .notes(product.getNotes())
            .prices(
                product.getPrices().stream()
                    .map(productPrice -> ProductPriceRecord.fromProductPriceEntity(productPrice))
                    .collect(Collectors.toList())
            )
            .barcodes(
                product.getBarcodes().stream()
                    .map(barcode -> ProductBarcodRecord.fromProductBarcodEntity(barcode))
                    .collect(Collectors.toList())
            )
            .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GroupRecord{
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
                .globalId(group.getGlobalId())
                .code(group.getCode())
                .name(group.getName())
                .notes(group.getNotes())
                .parent(fromGroupEntity(group.getParent()))
                .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UnitRecord{
        private Integer id;
        private UUID globalId;
        private String name;
        private List<UnitItemRecord> unitItems;
        
        public static UnitRecord fromUnitEntity(Unit unit){
            return UnitRecord.builder()
                .globalId(unit.getGlobalId())
                .name(unit.getName())
                .unitItems(unit.getUnitItems().stream()
                    .map(unitItem -> UnitItemRecord.fromUnitItemEntity(unitItem))
                    .collect(Collectors.toList())
                )
                .build();
        }

        public static UnitRecord fromUnitEntitySliced(Unit unit){
            return UnitRecord.builder()
                .globalId(unit.getGlobalId())
                .name(unit.getName())
                .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductPriceRecord{
        private Integer id;
        private UUID globalId;
        private PriceRecord priceRecord;
        private UnitItemRecord unitItemRecord;
        private BigDecimal price;

        public static ProductPriceRecord fromProductPriceEntity(ProductPrice productPrice){
            return ProductPriceRecord.builder()
                .globalId(productPrice.getGlobalId())
                .unitItemRecord(UnitItemRecord.fromUnitItemEntity(productPrice.getPriceUnit()))
                .priceRecord(PriceRecord.fromPriceEntity(productPrice.getPriceId()))
                .price(productPrice.getPrice())
                .build();
        }
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class PriceRecord{
            private Integer id;
            private UUID globalId;
            private String name;

            public static PriceRecord fromPriceEntity(Price price){
                return PriceRecord.builder()
                    .globalId(price.getGlobalId())
                    .name(price.getName())
                    .build();
            }
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductBarcodRecord{
        private Integer id;
        private UUID globalId;
        private UnitItemRecord unitItemRecord;
        private String barcode;

        public static ProductBarcodRecord fromProductBarcodEntity(ProductBarcode productBarcode){
            return ProductBarcodRecord.builder()
                .globalId(productBarcode.getGlobalId())
                .barcode(productBarcode.getBarcode())
                .unitItemRecord(UnitItemRecord.fromUnitItemEntity(productBarcode.getUnitItem()))
                .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class UnitItemRecord {
        private Integer id;
        private UUID globalId;  
        private String name;
        private Float fact;
        private Boolean isDef;
        private UnitRecord unit;

        public static UnitItemRecord fromUnitItemEntity(UnitItem unitItem){
            return UnitItemRecord.builder()
                .globalId(unitItem.getGlobalId())
                .name(unitItem.getName())
                .fact(unitItem.getFact())
                .isDef(unitItem.getIsDef())
                .unit(UnitRecord.fromUnitEntitySliced(unitItem.getUnit()))
                .build();
        }
    }


}
