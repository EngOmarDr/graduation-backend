package com.graduation_project.pos_app.Unit.services;

import com.graduation_project.pos_app.Unit.dtos.requests.CreateUnitRequest;
import com.graduation_project.pos_app.Unit.dtos.requests.UpdateUnitRequest;
import com.graduation_project.pos_app.Unit.dtos.responses.UnitResponse;

import java.util.List;
public interface UnitService {
    UnitResponse createUnit(CreateUnitRequest request);
    UnitResponse getUnitById(Integer id);
    List<UnitResponse> getAllUnits();
    UnitResponse updateUnit(Integer id, UpdateUnitRequest request);
    void deleteUnit(Integer id);
}
