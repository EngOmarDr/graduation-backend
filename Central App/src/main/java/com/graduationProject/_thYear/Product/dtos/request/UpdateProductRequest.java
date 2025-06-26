package com.graduationProject._thYear.Product.dtos.request;

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

    private Float minQty;

    private Float maxQty;

    private Float orderQty;


    private String notes;

    @NotNull(message = "product prices are required")
    private List<CreateProductPriceRequest> prices   ;

    @NotNull(message = "product barcodes are required")
    private List<CreateProductBarcodeRequest> barcodes   ;


}
