package com.graduationProject._thYear.Unit.services;

import com.graduationProject._thYear.EventSyncronization.Records.ProductRecord.UnitItemRecord;
import com.graduationProject._thYear.Unit.dtos.requests.CreateUnitItemRequest;
import com.graduationProject._thYear.Unit.dtos.requests.UpdateUnitItemRequest;
import com.graduationProject._thYear.Unit.dtos.responses.UnitItemResponse;
import com.graduationProject._thYear.Unit.models.Unit;
import com.graduationProject._thYear.Unit.models.UnitItem;

import java.util.List;

public interface UnitItemService {
    UnitItemResponse createUnitItem(CreateUnitItemRequest request);
    UnitItemResponse getUnitItemById(Integer id);
    List<UnitItemResponse> getAllUnitItems();
    List<UnitItemResponse> getUnitItemsByUnitId(Integer unitId);
    UnitItemResponse updateUnitItem(Integer id, UpdateUnitItemRequest request);
    void deleteUnitItem(Integer id);

    UnitItem saveOrUpdate(UnitItemRecord unitItem);
    UnitItem saveOrUpdateReference(UnitItemRecord unitItemRecord);
}
