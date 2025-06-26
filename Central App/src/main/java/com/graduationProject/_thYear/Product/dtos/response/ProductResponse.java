package com.graduationProject._thYear.Product.dtos.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class ProductResponse {
    private Integer id;
    private String code;
    private String name;
    private Integer groupId;
    private String image;
    private Byte type;
    private String typeName;
    private Integer defaultUnitId;
    private Float quantity;
    private Float minQty;
    private Float maxQty;
    private Float orderQty;
    private String notes;
    private List<ProductPriceResponse> prices;
    private List<ProductBarcodeResponse> barcodes;
}
