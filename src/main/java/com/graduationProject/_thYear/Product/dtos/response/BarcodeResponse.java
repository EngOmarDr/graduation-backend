package com.graduationProject._thYear.Product.dtos.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class BarcodeResponse {
    private Integer id;
    private String barcode;
}
