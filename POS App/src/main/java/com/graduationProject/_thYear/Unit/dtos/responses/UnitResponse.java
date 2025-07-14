package com.graduationProject._thYear.Unit.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class UnitResponse {
    private Integer id;
    private String name;
    private List<UnitItemResponse> unitItems;
}
