package com.graduation_project.pos_app.Product.dtos.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class PriceResponse {
    private Integer id;
    private String name;
    private List<ProductPriceResponse> productPrices;
}
