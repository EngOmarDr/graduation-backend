package com.graduationProject._thYear.Product.services;

import com.graduationProject._thYear.Product.dtos.request.CreateProductPriceRequest;
import com.graduationProject._thYear.Product.dtos.request.UpdateProductPriceRequest;
import com.graduationProject._thYear.Product.dtos.response.ProductPriceResponse;

import java.util.List;

public interface ProductPriceService {
    ProductPriceResponse createProductPrice(CreateProductPriceRequest request);
    ProductPriceResponse getProductPriceById(Integer id);
    List<ProductPriceResponse> getAllProductPrices();
    List<ProductPriceResponse> getProductPricesByProductId(Integer productId);
    List<ProductPriceResponse> getProductPricesByPriceId(Integer priceId);
    ProductPriceResponse updateProductPrice(Integer id, UpdateProductPriceRequest request);
    void deleteProductPrice(Integer id);
}
