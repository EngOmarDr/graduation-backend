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
    private String code;

    private String name;

    private MultipartFile image;

    private Integer groupId;

    private Byte type;

    private Integer defaultUnitId;

    private Float minQty;

    private Float maxQty;

    private Float orderQty;


    private String notes;

    private List<CreateProductPriceRequest> prices   ;

    private List<CreateProductBarcodeRequest> barcodes   ;


}
