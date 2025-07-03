package com.graduationProject._thYear.Unit.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUnitItemRequest {
    private Integer unitId;

    private String name;

    private Float fact;

    private Boolean isDef;
}
