package com.graduation_project.pos_app.Product.dtos.request;

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
public class CreateProductRequest {
    @NotNull(message = "Code is required")
    private String code;

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Category ID is required")
    private Integer categoryId;

    @NotNull(message = "Default unit ID is required")
    private Integer defaultUnitId;

    @NotNull(message = "Low quantity is required")
    private Float lowQty;
}
