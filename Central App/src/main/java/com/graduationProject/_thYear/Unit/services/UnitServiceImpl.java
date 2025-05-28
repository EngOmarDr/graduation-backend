package com.graduationProject._thYear.Unit.services;

import com.graduationProject._thYear.Unit.dtos.requests.CreateUnitItemRequest;
import com.graduationProject._thYear.Unit.dtos.requests.CreateUnitRequest;
import com.graduationProject._thYear.Unit.dtos.requests.UpdateUnitRequest;
import com.graduationProject._thYear.Unit.dtos.responses.UnitItemResponse;
import com.graduationProject._thYear.Unit.dtos.responses.UnitResponse;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import com.graduationProject._thYear.Unit.models.Unit;
import com.graduationProject._thYear.Unit.models.UnitItem;
import com.graduationProject._thYear.Unit.repositories.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;

    @Override
    public UnitResponse createUnit(CreateUnitRequest request) {
        if (unitRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Unit with name '" + request.getName() + "' already exists");
        }

        Unit unit = Unit.builder()
                .name(request.getName())
                .build();

        for (CreateUnitItemRequest itemRequest: request.getUnitItems()){
            unit.addUnitItem(
                    UnitItem.builder()
                    .unit(unit)
                    .name(itemRequest.getName())
                    .isDef(itemRequest.getIsDef())
                    .fact(itemRequest.getFact())
                    .build()    
                );
        }
        Unit savedUnit = unitRepository.save(unit);
        return convertToResponse(savedUnit);
    }

    @Override
    public UnitResponse getUnitById(Integer id) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found with id: " + id));
        return convertToResponse(unit);
    }

    @Override
    public List<UnitResponse> getAllUnits() {
        return unitRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UnitResponse updateUnit(Integer id, UpdateUnitRequest request) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found with id: " + id));

        // Check if name is being changed and validate uniqueness
        if (!unit.getName().equals(request.getName()) &&
                unitRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Unit with name '" + request.getName() + "' already exists");
        }

        unit.setName(request.getName());

        Unit updatedUnit = unitRepository.save(unit);
        return convertToResponse(updatedUnit);
    }

    @Override
    public void deleteUnit(Integer id) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found with id: " + id));

        // Handle relationships before deletion
        if (!unit.getUnitItems().isEmpty()) {
            unit.getUnitItems().forEach(item -> item.setUnit(null));
        }

        if (!unit.getProductPrices().isEmpty()) {
            unit.getProductPrices().forEach(price -> price.setPriceUnit(null));
        }

        if (!unit.getProducts().isEmpty()) {
            unit.getProducts().forEach(product -> product.setDefaultUnit(null));
        }

        unitRepository.delete(unit);
    }

    private UnitResponse convertToResponse(Unit unit) {
        return UnitResponse.builder()
                .id(unit.getId())
                .name(unit.getName())
                .unitItems(convertUnitItemsToResponse(unit.getUnitItems()))
                .build();
    }

    private List<UnitItemResponse> convertUnitItemsToResponse(List<UnitItem> unitItems) {
        return unitItems.stream()
                .map(this::convertUnitItemToResponse)
                .collect(Collectors.toList());
    }

    private UnitItemResponse convertUnitItemToResponse(UnitItem unitItem) {
        return UnitItemResponse.builder()
                .id(unitItem.getId())
                .unitId(unitItem.getUnit().getId())
                .unitName(unitItem.getUnit().getName())
                .name(unitItem.getName())
                .fact(unitItem.getFact())
                .isDef(unitItem.getIsDef())
                //.barcodes(unitItem.getBarcodes())
                .build();
    }
}
