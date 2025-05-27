package com.graduation_project.pos_app.Unit.dtos.responses;


import com.graduation_project.pos_app.Product.dtos.response.ProductBarcodeResponse;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class UnitItemResponse {
    private Integer id;
    private Integer unitId;
    private String unitName;
    private String name;
    private Float fact;
    private Boolean isDef;
    private List<ProductBarcodeResponse> barcodes;
}
