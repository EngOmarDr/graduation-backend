package com.graduationProject._thYear.Product.repositories;


import com.graduationProject._thYear.Product.models.ProductBarcode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductBarcodeRepository extends JpaRepository<ProductBarcode,Integer> {
    boolean existsByBarcode(String barcode);
    List<ProductBarcode> findByProductId(Integer productId);
    List<ProductBarcode> findAllByBarcode(String barcode);
}
