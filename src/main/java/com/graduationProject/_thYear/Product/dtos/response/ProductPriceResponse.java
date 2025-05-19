package com.graduationProject._thYear.Product.dtos.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ProductPriceResponse {
    private Integer id;
    private Float price;
    private Integer unitId;
    private String unitName;
}
