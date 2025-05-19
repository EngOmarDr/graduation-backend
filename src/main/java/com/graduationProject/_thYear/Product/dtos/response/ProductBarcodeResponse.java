package com.graduationProject._thYear.Product.dtos.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ProductBarcodeResponse {
    private Integer id;
    private String barcode;
    private Integer unitItemId;
    private String unitItemName;
}
