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
    private String groupName;
    private Integer defaultUnitId;
    private String defaultUnitName;
    private Float lowQty;
    private List<ProductPriceResponse> prices;
    private List<ProductBarcodeResponse> barcodes;
}
