package com.graduationProject._thYear.Product.repositories;


import com.graduationProject._thYear.Product.models.ProductBarcode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBarcodeRepository extends JpaRepository<ProductBarcode,Integer> {
}
