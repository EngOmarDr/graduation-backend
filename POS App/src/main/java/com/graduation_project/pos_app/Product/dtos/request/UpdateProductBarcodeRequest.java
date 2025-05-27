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
public class UpdateProductBarcodeRequest {
    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "Unit item ID is required")
    private Integer unitItemId;

    @NotNull(message = "Barcode is required")
    private String barcode;
}
