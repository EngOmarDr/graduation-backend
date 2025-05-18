package com.graduationProject._thYear.Product.dtos;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class BarcodeResponse {
    private Integer id;
    private String barcode;
}
