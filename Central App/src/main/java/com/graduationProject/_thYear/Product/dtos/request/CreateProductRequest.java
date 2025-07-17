package com.graduationProject._thYear.Product.dtos.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    private MultipartFile image;

    @NotNull(message = "Group ID is required")
    private Integer groupId;

    @NotNull(message = "Type is required")
    private Byte type;

    @NotNull(message = "Default unit ID is required")
    private Integer defaultUnitId;

    @DecimalMin(value = "0.0", inclusive = true, message = "minQty Must be positive")
    private Float minQty;

    @DecimalMin(value = "0.0", inclusive = true, message = "maxQty Must be positive")
    private Float maxQty;

    @DecimalMin(value = "0.0", inclusive = true, message = "orderQty Must be positive")
    private Float orderQty;

    private String notes;

    @NotNull(message = "product prices are required")
    private List<CreateProductPriceRequest> prices   ;

    @NotNull(message = "product barcodes are required")
    private List<CreateProductBarcodeRequest> barcodes   ;


}
