package com.graduationProject._thYear.Product.dtos.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

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

    @NotNull(message = "Unit Item ID is required")
    private Integer unitItemId;

    @NotNull(message = "Price value is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "price Must be positive")
    private BigDecimal price;
}
