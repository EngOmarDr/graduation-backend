package com.graduationProject._thYear.Product.services;

import com.graduationProject._thYear.EventSyncronization.Records.ProductRecord.ProductPriceRecord;
import com.graduationProject._thYear.Product.dtos.request.CreateProductPriceRequest;
import com.graduationProject._thYear.Product.dtos.request.UpdateProductPriceRequest;
import com.graduationProject._thYear.Product.dtos.response.ProductPriceResponse;
import com.graduationProject._thYear.Product.models.Price;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.models.ProductPrice;
import com.graduationProject._thYear.Product.repositories.PriceRepository;
import com.graduationProject._thYear.Product.repositories.ProductPriceRepository;
import com.graduationProject._thYear.Product.repositories.ProductRepository;
import com.graduationProject._thYear.Unit.models.UnitItem;
import com.graduationProject._thYear.Unit.repositories.UnitItemRepository;
import com.graduationProject._thYear.Unit.services.UnitItemService;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final PriceService priceService;
    private final UnitItemService unitItemService;

    private final UnitItemRepository unitItemRepository;

    @Override
    public ProductPriceResponse createProductPrice(CreateProductPriceRequest request) {
        // Fetch related entities first
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + request.getProductId()));

        Price price = priceRepository.findById(request.getPriceId())
                .orElseThrow(() -> new EntityNotFoundException("Price not found with id: " + request.getPriceId()));

        UnitItem unitItem = unitItemRepository.findById(request.getUnitItemId())
                .orElseThrow(() -> new EntityNotFoundException("UnitItem not found with id: " + request.getUnitItemId()));
        // Only check for duplicates if the product isn't brand new
        boolean exists = productPriceRepository.existsByProductId_IdAndPriceId_IdAndPriceUnit(product.getId(), price.getId(), unitItem);
        if (exists) {
            throw new IllegalArgumentException("Product price with these parameters already exists");
        }
        // Create and save product price
        ProductPrice productPrice = ProductPrice.builder()
                .productId(product)
                .priceId(price)
                .priceUnit(unitItem)
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

        // Update product reference
        if (request.getProductId() != null) {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            productPrice.setProductId(product);
        }

        // Update price reference
        if (request.getPriceId() != null) {
            Price price = priceRepository.findById(request.getPriceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Price not found"));
            productPrice.setPriceId(price);
        }

        // Update unit item
        if (request.getUnitItemId() != null) {
            UnitItem unitItem = unitItemRepository.findById(request.getUnitItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unit not found"));
            productPrice.setPriceUnit(unitItem);
        }

        if (request.getPrice() != null) {
            productPrice.setPrice(BigDecimal.valueOf(request.getPrice()));
        }

        ProductPrice updated = productPriceRepository.save(productPrice);
        return convertToResponse(updated);
    }

    @Override
    public void deleteProductPrice(Integer id) {
        ProductPrice productPrice = productPriceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product price not found with id: " + id));

        productPriceRepository.delete(productPrice);
    }

    @Override
    public ProductPrice saveOrUpdate(ProductPriceRecord productPriceRecord){
        ProductPrice productPrice = saveOrUpdateReference(productPriceRecord);
        productPriceRepository.save(productPrice);
        return productPrice;
    }
     @Override
    public List<ProductPrice> saveOrUpdateBulk(List<ProductPriceRecord> records){
        List<ProductPrice> models = records.stream()
            .map(record -> saveOrUpdateReference(record))
            .collect(Collectors.toList());
        productPriceRepository.saveAll(models);
        return models;
    }

    private ProductPrice saveOrUpdateReference(ProductPriceRecord productPriceRecord){
        ProductPrice productPrice = productPriceRepository.findByGlobalId(productPriceRecord.getGlobalId())
            .orElse(new ProductPrice());
        Price price = priceService.saveOrUpdate(productPriceRecord.getPriceRecord());
        UnitItem unitItem = unitItemService.saveOrUpdate(productPriceRecord.getUnitItemRecord());
        productPrice.toBuilder()
            .globalId(productPrice.getGlobalId())
            .priceUnit(unitItem)
            .priceId(price)
            .price(productPrice.getPrice())
            .build();
        return productPrice;
    }
    private ProductPriceResponse convertToResponse(ProductPrice productPrice) {
        return ProductPriceResponse.builder()
                .id(productPrice.getId())
                .productId(productPrice.getProductId().getId())
                .productName(productPrice.getProductId().getName())
                .priceId(productPrice.getPriceId().getId())
                .priceName(productPrice.getPriceId().getName())
                .unitItemId(productPrice.getPriceUnit().getId())
                .unitItemName(productPrice.getPriceUnit().getName())
                .price(productPrice.getPrice())
                .build();
    }
}
