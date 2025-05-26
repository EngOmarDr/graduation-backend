package com.graduationProject._thYear.Product.services;

import com.graduationProject._thYear.Product.dtos.request.CreatePriceRequest;
import com.graduationProject._thYear.Product.dtos.request.UpdatePriceRequest;
import com.graduationProject._thYear.Product.dtos.response.PriceResponse;

import java.util.List;

public interface PriceService {
    PriceResponse createPrice(CreatePriceRequest request);
    PriceResponse getPriceById(Integer id);
    List<PriceResponse> getAllPrices();
    PriceResponse updatePrice(Integer id, UpdatePriceRequest request);
    void deletePrice(Integer id);
}
