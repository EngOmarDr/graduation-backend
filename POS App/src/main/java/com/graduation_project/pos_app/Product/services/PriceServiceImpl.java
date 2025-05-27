package com.graduation_project.pos_app.Product.services;

import com.graduation_project.pos_app.Product.dtos.request.CreatePriceRequest;
import com.graduation_project.pos_app.Product.dtos.request.UpdatePriceRequest;
import com.graduation_project.pos_app.Product.dtos.response.PriceResponse;
import com.graduation_project.pos_app.Product.dtos.response.ProductPriceResponse;
import com.graduation_project.pos_app.Product.models.Price;
import com.graduation_project.pos_app.Product.models.ProductPrice;
import com.graduation_project.pos_app.Product.repositories.PriceRepository;
import com.graduation_project.pos_app.exceptionHandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService{

    private final PriceRepository priceRepository;

    @Override
    public PriceResponse createPrice(CreatePriceRequest request) {
        // Validate name uniqueness
        if (priceRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Price with name '" + request.getName() + "' already exists");
        }

        Price price = Price.builder()
                .name(request.getName())
                .build();

        Price savedPrice = priceRepository.save(price);
        return convertToResponse(savedPrice);
    }

    @Override
    public PriceResponse getPriceById(Integer id) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Price not found with id: " + id));
        return convertToResponse(price);
    }

    @Override
    public List<PriceResponse> getAllPrices() {
        return priceRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PriceResponse updatePrice(Integer id, UpdatePriceRequest request) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Price not found with id: " + id));

        // Check if name is being changed and validate uniqueness
        if (!price.getName().equals(request.getName()) &&
                priceRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Price with name '" + request.getName() + "' already exists");
        }

        price.setName(request.getName());

        Price updatedPrice = priceRepository.save(price);
        return convertToResponse(updatedPrice);
    }

    @Override
    public void deletePrice(Integer id) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Price not found with id: " + id));

        // Check if price has any product prices before deletion
        if (!price.getProductPrices().isEmpty()) {
            throw new IllegalStateException("Cannot delete price with associated product prices");
        }

        priceRepository.delete(price);
    }

    private PriceResponse convertToResponse(Price price) {
        return PriceResponse.builder()
                .id(price.getId())
                .name(price.getName())
                .productPrices(convertProductPricesToResponse(price.getProductPrices()))
                .build();
    }

    private List<ProductPriceResponse> convertProductPricesToResponse(List<ProductPrice> productPrices) {
        return productPrices.stream()
                .map(this::convertProductPriceToResponse)
                .collect(Collectors.toList());
    }

    private ProductPriceResponse convertProductPriceToResponse(ProductPrice productPrice) {
        return ProductPriceResponse.builder()
                .id(productPrice.getId())
                .productId(productPrice.getProductId().getId())
                .productName(productPrice.getProductId().getName())
                .price(productPrice.getPrice())
                .unitId(productPrice.getPriceUnit().getId())
                .unitName(productPrice.getPriceUnit().getName())
                .build();
    }
}
