package com.graduationProject._thYear.Product.services;

import com.graduationProject._thYear.EventSyncronization.Records.ProductRecord;
import com.graduationProject._thYear.Product.dtos.request.CreateProductRequest;
import com.graduationProject._thYear.Product.dtos.request.UpdateProductRequest;
import com.graduationProject._thYear.Product.dtos.response.ProductResponse;
import com.graduationProject._thYear.Product.models.Product;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);
    ProductResponse getProductById(Integer id);
    List<ProductResponse> getAllProducts();
    ProductResponse updateProduct(Integer id, UpdateProductRequest request);
    void deleteProduct(Integer id);

    List<ProductResponse> getByBarcode(String barcode);

    List<ProductResponse> searchProducts(String searchTerm);

    void importFromExcel(MultipartFile file);

//    List<ProductResponse> getProductsByWarehouse(Integer warehouseId);
    Product saveOrUpdate(ProductRecord productRecord);
    List<Product> saveOrUpdateBulk(List<ProductRecord> records);

}
