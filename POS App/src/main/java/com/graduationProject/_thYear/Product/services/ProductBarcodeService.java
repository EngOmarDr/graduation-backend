package com.graduationProject._thYear.Product.services;

import com.graduationProject._thYear.Product.dtos.request.CreateProductBarcodeRequest;
import com.graduationProject._thYear.Product.dtos.request.UpdateProductBarcodeRequest;
import com.graduationProject._thYear.Product.dtos.response.ProductBarcodeResponse;

import java.util.List;

public interface ProductBarcodeService {
    ProductBarcodeResponse createProductBarcode(CreateProductBarcodeRequest request);
    ProductBarcodeResponse getProductBarcodeById(Integer id);
    List<ProductBarcodeResponse> getAllProductBarcodes();
    List<ProductBarcodeResponse> getProductBarcodesByProductId(Integer productId);
    ProductBarcodeResponse updateProductBarcode(Integer id, UpdateProductBarcodeRequest request);
    void deleteProductBarcode(Integer id);


}
