package com.graduation_project.pos_app.Product.dtos.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ProductPriceResponse {
    private Integer id;
    private Integer productId;
    private String productName;
    private Integer priceId;
    private String priceName;
    private Integer unitId;
    private String unitName;
    private Float price;
}
