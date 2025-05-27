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
public class CreateProductPriceRequest {
    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "Price ID is required")
    private Integer priceId;

    @NotNull(message = "Unit ID is required")
    private Integer unitId;

    @NotNull(message = "Price value is required")
    private Float price;
}
