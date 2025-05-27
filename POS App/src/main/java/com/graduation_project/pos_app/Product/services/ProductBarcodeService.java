package com.graduation_project.pos_app.Product.services;

import com.graduation_project.pos_app.Product.dtos.request.CreateProductBarcodeRequest;
import com.graduation_project.pos_app.Product.dtos.request.UpdateProductBarcodeRequest;
import com.graduation_project.pos_app.Product.dtos.response.ProductBarcodeResponse;

import java.util.List;

public interface ProductBarcodeService {
    ProductBarcodeResponse createProductBarcode(CreateProductBarcodeRequest request);
    ProductBarcodeResponse getProductBarcodeById(Integer id);
    List<ProductBarcodeResponse> getAllProductBarcodes();
    List<ProductBarcodeResponse> getProductBarcodesByProductId(Integer productId);
    ProductBarcodeResponse updateProductBarcode(Integer id, UpdateProductBarcodeRequest request);
    void deleteProductBarcode(Integer id);


}
