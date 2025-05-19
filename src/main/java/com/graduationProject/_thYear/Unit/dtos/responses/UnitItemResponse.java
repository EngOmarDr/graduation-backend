package com.graduationProject._thYear.Unit.dtos.responses;

import com.graduationProject._thYear.Product.dtos.response.BarcodeResponse;
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
    private List<BarcodeResponse> barcodes;
}
