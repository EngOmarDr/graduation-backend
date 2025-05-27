package com.graduation_project.pos_app.Product.controllers;

import com.graduation_project.pos_app.Product.dtos.request.CreateProductBarcodeRequest;
import com.graduation_project.pos_app.Product.dtos.request.UpdateProductBarcodeRequest;
import com.graduation_project.pos_app.Product.dtos.response.ProductBarcodeResponse;
import com.graduation_project.pos_app.Product.services.ProductBarcodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-barcodes")
@RequiredArgsConstructor
public class ProductBarcodeController {

    private final ProductBarcodeService productBarcodeService;

    @PostMapping
    public ResponseEntity<ProductBarcodeResponse> createProductBarcode(
            @Valid @RequestBody CreateProductBarcodeRequest request) {
        ProductBarcodeResponse response = productBarcodeService.createProductBarcode(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBarcodeResponse> getProductBarcodeById(@PathVariable Integer id) {
        ProductBarcodeResponse response = productBarcodeService.getProductBarcodeById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductBarcodeResponse>> getAllProductBarcodes() {
        List<ProductBarcodeResponse> responses = productBarcodeService.getAllProductBarcodes();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductBarcodeResponse>> getProductBarcodesByProductId(@PathVariable Integer productId) {
        List<ProductBarcodeResponse> responses = productBarcodeService.getProductBarcodesByProductId(productId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductBarcodeResponse> updateProductBarcode(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateProductBarcodeRequest request) {
        ProductBarcodeResponse response = productBarcodeService.updateProductBarcode(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductBarcode(@PathVariable Integer id) {
        productBarcodeService.deleteProductBarcode(id);
        return ResponseEntity.noContent().build();
    }
}
