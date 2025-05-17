package com.graduationProject._thYear.repository.product;

import com.graduationProject._thYear.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
}
