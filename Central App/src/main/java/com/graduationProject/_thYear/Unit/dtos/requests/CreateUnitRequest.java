package com.graduationProject._thYear.Unit.dtos.requests;

import java.util.List;

import com.graduationProject._thYear.Unit.models.UnitItem;

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
public class CreateUnitRequest {

    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "unit items are required")
    private List<CreateUnitItemRequest> unitItems;
}
