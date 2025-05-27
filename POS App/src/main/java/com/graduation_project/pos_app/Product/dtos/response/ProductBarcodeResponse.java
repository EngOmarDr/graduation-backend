package com.graduation_project.pos_app.Product.dtos.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ProductBarcodeResponse {
    private Integer id;
    private Integer productId;
    private String productName;
    private Integer unitItemId;
    private String unitItemName;
    private String barcode;
}
