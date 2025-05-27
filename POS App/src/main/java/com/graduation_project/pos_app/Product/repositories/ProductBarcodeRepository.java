package com.graduation_project.pos_app.Product.repositories;


import com.graduation_project.pos_app.Product.models.ProductBarcode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductBarcodeRepository extends JpaRepository<ProductBarcode,Integer> {
    boolean existsByBarcode(String barcode);
    List<ProductBarcode> findByProductId(Integer productId);
}
