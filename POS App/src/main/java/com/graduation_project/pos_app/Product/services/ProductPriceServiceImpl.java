package com.graduation_project.pos_app.Product.services;

import com.graduation_project.pos_app.Product.dtos.request.CreateProductPriceRequest;
import com.graduation_project.pos_app.Product.dtos.request.UpdateProductPriceRequest;
import com.graduation_project.pos_app.Product.dtos.response.ProductPriceResponse;
import com.graduation_project.pos_app.Product.models.Price;
import com.graduation_project.pos_app.Product.models.Product;
import com.graduation_project.pos_app.Product.models.ProductPrice;
import com.graduation_project.pos_app.Product.repositories.PriceRepository;
import com.graduation_project.pos_app.Product.repositories.ProductPriceRepository;
import com.graduation_project.pos_app.Product.repositories.ProductRepository;
import com.graduation_project.pos_app.Unit.models.Unit;
import com.graduation_project.pos_app.Unit.repositories.UnitRepository;
import com.graduation_project.pos_app.exceptionHandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
//
@Service
@Transactional
@RequiredArgsConstructor
public class ProductPriceServiceImpl implements ProductPriceService{
    private final ProductPriceRepository productPriceRepository;
    private final ProductRepository productRepository;
    private final PriceRepository priceRepository;
    private final UnitRepository unitRepository;

    @Override
    public ProductPriceResponse createProductPrice(CreateProductPriceRequest request) {
        // Check if combination already exists
        Unit unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found with id: " + request.getUnitId()));

        if (productPriceRepository.existsByProductId_IdAndPriceId_IdAndPriceUnit(
                request.getProductId(), request.getPriceId(), unit)) {
            throw new IllegalArgumentException("Product price with this combination already exists");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        Price price = priceRepository.findById(request.getPriceId())
                .orElseThrow(() -> new ResourceNotFoundException("Price not found with id: " + request.getPriceId()));

        // Verify unit item belongs to product's default unit
        if (!unit.getId().equals(product.getDefaultUnit().getId())) {
            throw new IllegalArgumentException("Unit does not belong to product's default unit");
        }

        ProductPrice productPrice = ProductPrice.builder()
                .productId(product)
                .priceId(price)
                .priceUnit(unit)
                .price(request.getPrice())
                .build();

        ProductPrice savedProductPrice = productPriceRepository.save(productPrice);
        return convertToResponse(savedProductPrice);
    }

    @Override
    public ProductPriceResponse getProductPriceById(Integer id) {
        ProductPrice productPrice = productPriceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product price not found with id: " + id));
        return convertToResponse(productPrice);
    }

    @Override
    public List<ProductPriceResponse> getAllProductPrices() {
        return productPriceRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductPriceResponse> getProductPricesByProductId(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        return productPriceRepository.findByProductId_Id(productId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductPriceResponse> getProductPricesByPriceId(Integer priceId) {
        if (!priceRepository.existsById(priceId)) {
            throw new ResourceNotFoundException("Price not found with id: " + priceId);
        }

        return productPriceRepository.findByPriceId_Id(priceId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductPriceResponse updateProductPrice(Integer id, UpdateProductPriceRequest request) {
        ProductPrice productPrice = productPriceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product price not found with id: " + id));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        Price price = priceRepository.findById(request.getPriceId())
                .orElseThrow(() -> new ResourceNotFoundException("Price not found with id: " + request.getPriceId()));

        Unit unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found with id: " + request.getUnitId()));

        // Check if combination already exists for another record
        if (!(productPrice.getProductId().getId().equals(request.getProductId()) ||
                !(productPrice.getPriceId().getId().equals(request.getPriceId())) ||
                !(productPrice.getPriceUnit().getId().equals(request.getUnitId())))) {

            if (productPriceRepository.existsByProductId_IdAndPriceId_IdAndPriceUnit(
                    request.getProductId(), request.getPriceId(), unit)) {
                throw new IllegalArgumentException("Product price with this combination already exists");
            }
        }

        productPrice.setProductId(product);
        productPrice.setPriceId(price);
        productPrice.setPriceUnit(unit);
        productPrice.setPrice(request.getPrice());

        ProductPrice updatedProductPrice = productPriceRepository.save(productPrice);
        return convertToResponse(updatedProductPrice);
    }

    @Override
    public void deleteProductPrice(Integer id) {
        ProductPrice productPrice = productPriceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product price not found with id: " + id));

        productPriceRepository.delete(productPrice);
    }

    private ProductPriceResponse convertToResponse(ProductPrice productPrice) {
        return ProductPriceResponse.builder()
                .id(productPrice.getId())
                .productId(productPrice.getProductId().getId())
                .productName(productPrice.getProductId().getName())
                .priceId(productPrice.getPriceId().getId())
                .priceName(productPrice.getPriceId().getName())
                .unitId(productPrice.getPriceUnit().getId())
                .unitName(productPrice.getPriceUnit().getName())
                .price(productPrice.getPrice())
                .build();
    }
}
