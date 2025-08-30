package com.graduationProject._thYear.Unit.services;

import com.graduationProject._thYear.EventSyncronization.Records.ProductRecord.GroupRecord;
import com.graduationProject._thYear.EventSyncronization.Records.ProductRecord.UnitRecord;
import com.graduationProject._thYear.Group.models.Group;
import com.graduationProject._thYear.Unit.dtos.requests.CreateUnitRequest;
import com.graduationProject._thYear.Unit.dtos.requests.UpdateUnitRequest;
import com.graduationProject._thYear.Unit.dtos.responses.UnitResponse;
import com.graduationProject._thYear.Unit.models.Unit;

import java.util.List;
public interface UnitService {
    UnitResponse createUnit(CreateUnitRequest request);
    UnitResponse getUnitById(Integer id);
    List<UnitResponse> getAllUnits();
    UnitResponse updateUnit(Integer id, UpdateUnitRequest request);
    void deleteUnit(Integer id);
    Unit saveOrUpdate(UnitRecord groupRecord);
    Unit saveOrUpdateReference(UnitRecord unitRecord);
    Unit saveOrUpdateWithChildren(UnitRecord unitRecord);
}
