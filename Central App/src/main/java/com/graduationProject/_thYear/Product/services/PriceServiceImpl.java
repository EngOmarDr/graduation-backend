package com.graduationProject._thYear.Product.services;

import com.graduationProject._thYear.EventSyncronization.Records.ProductRecord.ProductPriceRecord;
import com.graduationProject._thYear.EventSyncronization.Records.ProductRecord.ProductPriceRecord.PriceRecord;
import com.graduationProject._thYear.Product.dtos.request.CreatePriceRequest;
import com.graduationProject._thYear.Product.dtos.request.UpdatePriceRequest;
import com.graduationProject._thYear.Product.dtos.response.PriceResponse;
import com.graduationProject._thYear.Product.dtos.response.ProductPriceResponse;
import com.graduationProject._thYear.Product.models.Price;
import com.graduationProject._thYear.Product.models.ProductPrice;
import com.graduationProject._thYear.Product.repositories.PriceRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
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

        if (request.getName() != null && !price.getName().equals(request.getName())) {
            if (priceRepository.existsByName(request.getName())) {
                throw new IllegalArgumentException("Price with name '" + request.getName() + "' already exists");
            }
            price.setName(request.getName());
        }

        Price updated = priceRepository.save(price);
        return convertToResponse(updated);
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

    @Override
    public Price saveOrUpdate(PriceRecord priceRecord){
        Price price = priceRepository.findByGlobalId(priceRecord.getGlobalId())
            .orElse(new Price());
        price = price.toBuilder()
            .globalId(priceRecord.getGlobalId())
            .name(priceRecord.getName())
            .build();
        priceRepository.save(price);
        return price;
    }
    private PriceResponse convertToResponse(Price price) {
        return PriceResponse.builder()
                .id(price.getId())
                .name(price.getName())
                //.productPrices(convertProductPricesToResponse(price.getProductPrices()))
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
                .unitItemId(productPrice.getPriceUnit().getId())
                .unitItemName(productPrice.getPriceUnit().getName())
                .build();
    }
}
