package com.graduation_project.pos_app.Product.services;

import com.graduation_project.pos_app.Product.dtos.request.CreatePriceRequest;
import com.graduation_project.pos_app.Product.dtos.request.UpdatePriceRequest;
import com.graduation_project.pos_app.Product.dtos.response.PriceResponse;

import java.util.List;

public interface PriceService {
    PriceResponse createPrice(CreatePriceRequest request);
    PriceResponse getPriceById(Integer id);
    List<PriceResponse> getAllPrices();
    PriceResponse updatePrice(Integer id, UpdatePriceRequest request);
    void deletePrice(Integer id);
}
