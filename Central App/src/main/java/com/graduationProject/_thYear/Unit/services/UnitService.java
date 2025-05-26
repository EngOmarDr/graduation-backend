package com.graduationProject._thYear.Unit.services;

import com.graduationProject._thYear.Unit.dtos.requests.CreateUnitRequest;
import com.graduationProject._thYear.Unit.dtos.requests.UpdateUnitRequest;
import com.graduationProject._thYear.Unit.dtos.responses.UnitResponse;

import java.util.List;
public interface UnitService {
    UnitResponse createUnit(CreateUnitRequest request);
    UnitResponse getUnitById(Integer id);
    List<UnitResponse> getAllUnits();
    UnitResponse updateUnit(Integer id, UpdateUnitRequest request);
    void deleteUnit(Integer id);
}
