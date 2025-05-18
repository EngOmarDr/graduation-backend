package com.graduationProject._thYear.Unit.dtos.responses;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class UnitItemResponse {
    private Integer id;
    private Integer unitId;
    private String unitName;
    private String name;
    private Float fact;
    private Boolean isDef;
    // private List<BarcodeResponse> barcodes;
}
