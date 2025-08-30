package com.graduationProject._thYear.Product.services;

import com.graduationProject._thYear.EventSyncronization.Records.ProductRecord.ProductPriceRecord;
import com.graduationProject._thYear.Product.dtos.request.CreateProductPriceRequest;
import com.graduationProject._thYear.Product.dtos.request.UpdateProductPriceRequest;
import com.graduationProject._thYear.Product.dtos.response.ProductPriceResponse;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.models.ProductPrice;

import java.util.List;

public interface ProductPriceService {
    ProductPriceResponse createProductPrice(CreateProductPriceRequest request);
    ProductPriceResponse getProductPriceById(Integer id);
    List<ProductPriceResponse> getAllProductPrices();
    List<ProductPriceResponse> getProductPricesByProductId(Integer productId);
    List<ProductPriceResponse> getProductPricesByPriceId(Integer priceId);
    ProductPriceResponse updateProductPrice(Integer id, UpdateProductPriceRequest request);
    void deleteProductPrice(Integer id);

    // ProductPrice saveOrUpdate(ProductPriceRecord productPriceRecord);
    // List<ProductPrice> saveOrUpdateBulk(List<ProductPriceRecord> records);
    List<ProductPrice> saveOrUpdateBulk(List<ProductPriceRecord> records, Product product);
}
