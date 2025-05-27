package com.graduation_project.pos_app.Product.controllers;
import com.graduation_project.pos_app.Product.dtos.request.CreateProductPriceRequest;
import com.graduation_project.pos_app.Product.dtos.request.UpdateProductPriceRequest;
import com.graduation_project.pos_app.Product.dtos.response.ProductPriceResponse;
import com.graduation_project.pos_app.Product.services.ProductPriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-prices")
@RequiredArgsConstructor
public class ProductPriceController {
    private final ProductPriceService productPriceService;

    @PostMapping
    public ResponseEntity<ProductPriceResponse> createProductPrice(
            @Valid @RequestBody CreateProductPriceRequest request) {
        ProductPriceResponse response = productPriceService.createProductPrice(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductPriceResponse> getProductPriceById(@PathVariable Integer id) {
        ProductPriceResponse response = productPriceService.getProductPriceById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductPriceResponse>> getAllProductPrices() {
        List<ProductPriceResponse> responses = productPriceService.getAllProductPrices();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductPriceResponse>> getProductPricesByProductId(@PathVariable Integer productId) {
        List<ProductPriceResponse> responses = productPriceService.getProductPricesByProductId(productId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/price/{priceId}")
    public ResponseEntity<List<ProductPriceResponse>> getProductPricesByPriceId(@PathVariable Integer priceId) {
        List<ProductPriceResponse> responses = productPriceService.getProductPricesByPriceId(priceId);
            return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductPriceResponse> updateProductPrice(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateProductPriceRequest request) {
        ProductPriceResponse response = productPriceService.updateProductPrice(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductPrice(@PathVariable Integer id) {
        productPriceService.deleteProductPrice(id);
        return ResponseEntity.noContent().build();
    }
}
