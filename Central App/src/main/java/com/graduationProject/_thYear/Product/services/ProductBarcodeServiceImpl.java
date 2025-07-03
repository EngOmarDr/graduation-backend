package com.graduationProject._thYear.Product.services;
import com.graduationProject._thYear.Product.dtos.request.CreateProductBarcodeRequest;
import com.graduationProject._thYear.Product.dtos.request.UpdateProductBarcodeRequest;
import com.graduationProject._thYear.Product.dtos.response.ProductBarcodeResponse;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.models.ProductBarcode;
import com.graduationProject._thYear.Product.repositories.ProductBarcodeRepository;
import com.graduationProject._thYear.Product.repositories.ProductRepository;
import com.graduationProject._thYear.Unit.models.UnitItem;
import com.graduationProject._thYear.Unit.repositories.UnitItemRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductBarcodeServiceImpl implements ProductBarcodeService{
    private final ProductBarcodeRepository productBarcodeRepository;
    private final ProductRepository productRepository;
    private final UnitItemRepository unitItemRepository;

    @Override
    public ProductBarcodeResponse createProductBarcode(CreateProductBarcodeRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        UnitItem unitItem = unitItemRepository.findById(request.getUnitItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit item not found with id: " + request.getUnitItemId()));

        // Verify unit item belongs to product's default unit
        if (!unitItem.getUnit().getId().equals(product.getDefaultUnit().getId())) {
            throw new IllegalArgumentException("Unit item does not belong to product's default unit");
        }

        ProductBarcode productBarcode = ProductBarcode.builder()
                .product(product)
                .unitItem(unitItem)
                .barcode(request.getBarcode())
                .build();

        ProductBarcode savedBarcode = productBarcodeRepository.save(productBarcode);
        return convertToResponse(savedBarcode);
    }

    @Override
    public ProductBarcodeResponse getProductBarcodeById(Integer id) {
        ProductBarcode productBarcode = productBarcodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product barcode not found with id: " + id));
        return convertToResponse(productBarcode);
    }

    @Override
    public List<ProductBarcodeResponse> getAllProductBarcodes() {
        return productBarcodeRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductBarcodeResponse> getProductBarcodesByProductId(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        return productBarcodeRepository.findByProductId(productId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    @Override
    public ProductBarcodeResponse updateProductBarcode(Integer id, UpdateProductBarcodeRequest request) {
        ProductBarcode productBarcode = productBarcodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product barcode not found with id: " + id));

        // Barcode (check uniqueness)
        if (request.getBarcode() != null && !request.getBarcode().equals(productBarcode.getBarcode())) {
            if (productBarcodeRepository.existsByBarcode(request.getBarcode())) {
                throw new IllegalArgumentException("Barcode '" + request.getBarcode() + "' already exists");
            }
            productBarcode.setBarcode(request.getBarcode());
        }

        // Product
        if (request.getProductId() != null) {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            productBarcode.setProduct(product);
        }

        // Unit item
        if (request.getUnitItemId() != null) {
            UnitItem unitItem = unitItemRepository.findById(request.getUnitItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unit item not found"));

            // Validate relation
            if (!productBarcode.getProduct().getDefaultUnit().getId().equals(unitItem.getUnit().getId())) {
                throw new IllegalArgumentException("Unit item does not belong to product's default unit");
            }
            productBarcode.setUnitItem(unitItem);
        }

        ProductBarcode updated = productBarcodeRepository.save(productBarcode);
        return convertToResponse(updated);
    }


    @Override
    public void deleteProductBarcode(Integer id) {
        ProductBarcode productBarcode = productBarcodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product barcode not found with id: " + id));

        productBarcodeRepository.delete(productBarcode);
    }

    private ProductBarcodeResponse convertToResponse(ProductBarcode productBarcode) {
        return ProductBarcodeResponse.builder()
                .id(productBarcode.getId())
                .productId(productBarcode.getProduct().getId())
                .productName(productBarcode.getProduct().getName())
                .unitItemId(productBarcode.getUnitItem().getId())
                .unitItemName(productBarcode.getUnitItem().getName())
                .barcode(productBarcode.getBarcode())
                .build();
    }
}
