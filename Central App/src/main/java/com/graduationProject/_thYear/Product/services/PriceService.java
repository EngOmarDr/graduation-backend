package com.graduationProject._thYear.Product.services;

import com.graduationProject._thYear.EventSyncronization.Records.ProductRecord.ProductPriceRecord.PriceRecord;
import com.graduationProject._thYear.Product.dtos.request.CreatePriceRequest;
import com.graduationProject._thYear.Product.dtos.request.UpdatePriceRequest;
import com.graduationProject._thYear.Product.dtos.response.PriceResponse;
import com.graduationProject._thYear.Product.models.Price;

import java.util.List;

public interface PriceService {
    PriceResponse createPrice(CreatePriceRequest request);
    PriceResponse getPriceById(Integer id);
    List<PriceResponse> getAllPrices();
    PriceResponse updatePrice(Integer id, UpdatePriceRequest request);
    void deletePrice(Integer id);
    Price saveOrUpdate(PriceRecord priceRecord);
}

