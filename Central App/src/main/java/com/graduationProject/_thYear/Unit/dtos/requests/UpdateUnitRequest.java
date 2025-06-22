package com.graduationProject._thYear.Unit.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUnitRequest {

    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "unit items are required")
    private List<UpdateUnitItemRequest> unitItems;
}
