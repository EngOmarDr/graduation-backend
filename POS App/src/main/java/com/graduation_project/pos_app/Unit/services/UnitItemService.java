package com.graduation_project.pos_app.Unit.services;

import com.graduation_project.pos_app.Unit.dtos.requests.CreateUnitItemRequest;
import com.graduation_project.pos_app.Unit.dtos.requests.UpdateUnitItemRequest;
import com.graduation_project.pos_app.Unit.dtos.responses.UnitItemResponse;

import java.util.List;

public interface UnitItemService {
    UnitItemResponse createUnitItem(CreateUnitItemRequest request);
    UnitItemResponse getUnitItemById(Integer id);
    List<UnitItemResponse> getAllUnitItems();
    List<UnitItemResponse> getUnitItemsByUnitId(Integer unitId);
    UnitItemResponse updateUnitItem(Integer id, UpdateUnitItemRequest request);
    void deleteUnitItem(Integer id);
}
