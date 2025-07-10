package com.graduationProject._thYear.Product.controllers;

import com.graduationProject._thYear.Product.dtos.request.CreateProductRequest;
import com.graduationProject._thYear.Product.dtos.request.UpdateProductRequest;
import com.graduationProject._thYear.Product.dtos.response.ProductResponse;
import com.graduationProject._thYear.Product.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> createProduct(@ModelAttribute  @Valid CreateProductRequest request) {
        ProductResponse createdProduct = productService.createProduct(request);
        return ResponseEntity.ok(createdProduct);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> responses = productService.getAllProducts();
        return ResponseEntity.ok(responses);
    }

    @PutMapping(value = "/{id}" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Integer id,
            @Valid @ModelAttribute UpdateProductRequest request) {
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-barcode")
    public ResponseEntity<List<ProductResponse>> getByBarcodeParam(@RequestParam String barcode) {
        return ResponseEntity.ok(productService.getByBarcode(barcode));
    }

}
