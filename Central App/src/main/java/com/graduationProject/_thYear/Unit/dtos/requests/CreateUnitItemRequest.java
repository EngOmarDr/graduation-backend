package com.graduationProject._thYear.Unit.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
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
public class CreateUnitItemRequest {
    @NotNull(message = "Unit ID is required")
    private Integer unitId;

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Conversion factor is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "fact must be positive")
    private Float fact;

    private Boolean isDef;
}
