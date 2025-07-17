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
public class UpdateProductRequest {
    private String code;

    private String name;

    private MultipartFile image;

    private Integer groupId;

    private Byte type;

    private Integer defaultUnitId;

    @DecimalMin(value = "0.0", inclusive = true, message = "minQty Must be positive")
    private Float minQty;

    @DecimalMin(value = "0.0", inclusive = true, message = "maxQty Must be positive")
    private Float maxQty;

    @DecimalMin(value = "0.0", inclusive = true, message = "orderQty Must be positive")
    private Float orderQty;


    private String notes;

    private List<CreateProductPriceRequest> prices   ;

    private List<CreateProductBarcodeRequest> barcodes   ;


}
