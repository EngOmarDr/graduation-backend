package com.graduationProject._thYear.Product.services;

import com.graduationProject._thYear.Group.models.Group;
import com.graduationProject._thYear.Group.repositories.GroupRepository;
import com.graduationProject._thYear.Product.dtos.request.CreateProductRequest;
import com.graduationProject._thYear.Product.dtos.request.UpdateProductRequest;
import com.graduationProject._thYear.Product.dtos.response.ProductBarcodeResponse;
import com.graduationProject._thYear.Product.dtos.response.ProductPriceResponse;
import com.graduationProject._thYear.Product.dtos.response.ProductResponse;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.models.ProductBarcode;
import com.graduationProject._thYear.Product.models.ProductPrice;
import com.graduationProject._thYear.Product.repositories.ProductRepository;
import com.graduationProject._thYear.Unit.models.Unit;
import com.graduationProject._thYear.Unit.repositories.UnitRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final GroupRepository categoryRepository;
    private final UnitRepository unitRepository;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        // Validate unique code and name
        if (productRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Product with code '" + request.getCode() + "' already exists");
        }
        if (productRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Product with name '" + request.getName() + "' already exists");
        }

        Group category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Unit defaultUnit = unitRepository.findById(request.getDefaultUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found with id: " + request.getDefaultUnitId()));

        Product product = Product.builder()
                .code(request.getCode())
                .name(request.getName())
                .categoryId(category)
                .defaultUnit(defaultUnit)
                .lowQty(request.getLowQty())
                .build();

        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    @Override
    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return convertToResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateProduct(Integer id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Validate unique code and name if changed
        if (!product.getCode().equals(request.getCode()) &&
                productRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Product with code '" + request.getCode() + "' already exists");
        }
        if (!product.getName().equals(request.getName()) &&
                productRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Product with name '" + request.getName() + "' already exists");
        }

        Group category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Unit defaultUnit = unitRepository.findById(request.getDefaultUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found with id: " + request.getDefaultUnitId()));

        product.setCode(request.getCode());
        product.setName(request.getName());
        product.setCategoryId(category);
        product.setDefaultUnit(defaultUnit);
        product.setLowQty(request.getLowQty());

        Product updatedProduct = productRepository.save(product);
        return convertToResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Check if product has any prices or barcodes
        if (!product.getPrices().isEmpty()) {
            throw new IllegalStateException("Cannot delete product with associated prices");
        }
        if (!product.getBarcodes().isEmpty()) {
            throw new IllegalStateException("Cannot delete product with associated barcodes");
        }

        productRepository.delete(product);
    }

    private ProductResponse convertToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .categoryId(product.getCategoryId().getId())
                .categoryName(product.getCategoryId().getName())
                .defaultUnitId(product.getDefaultUnit().getId())
                .defaultUnitName(product.getDefaultUnit().getName())
                .lowQty(product.getLowQty())
                .prices(convertPricesToResponse(product.getPrices()))
                .barcodes(convertBarcodesToResponse(product.getBarcodes()))
                .build();
    }

    private List<ProductPriceResponse> convertPricesToResponse(List<ProductPrice> prices) {
        return prices.stream()
                .map(this::convertPriceToResponse)
                .collect(Collectors.toList());
    }

    private ProductPriceResponse convertPriceToResponse(ProductPrice price) {
        return ProductPriceResponse.builder()
                .id(price.getId())
                .price(price.getPrice())
                .unitId(price.getPriceUnit().getId())
                .unitName(price.getPriceUnit().getName())
                .build();
    }

    private List<ProductBarcodeResponse> convertBarcodesToResponse(List<ProductBarcode> barcodes) {
        return barcodes.stream()
                .map(this::convertBarcodeToResponse)
                .collect(Collectors.toList());
    }

    private ProductBarcodeResponse convertBarcodeToResponse(ProductBarcode barcode) {
        return ProductBarcodeResponse.builder()
                .id(barcode.getId())
                .barcode(barcode.getBarcode())
                .unitItemId(barcode.getUnitItem().getId())
                .unitItemName(barcode.getUnitItem().getName())
                .build();
    }
}
