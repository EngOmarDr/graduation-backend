package com.graduationProject._thYear.Unit.services;

import com.graduationProject._thYear.Unit.dtos.requests.CreateUnitItemRequest;
import com.graduationProject._thYear.Unit.dtos.requests.CreateUnitRequest;
import com.graduationProject._thYear.Unit.dtos.requests.UpdateUnitItemRequest;
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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;

    @Override
    public UnitResponse createUnit(CreateUnitRequest request) {
        // Validate unit name uniqueness
        if (unitRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Unit with name '" + request.getName() + "' already exists");
        }

        // Validate unit item names uniqueness
        validateUnitItemNames(request.getUnitItems());

        Unit unit = Unit.builder()
                .name(request.getName())
                .build();

        // Process unit items
        for (CreateUnitItemRequest itemRequest : request.getUnitItems()) {
            UnitItem unitItem = UnitItem.builder()
                    .unit(unit)
                    .name(itemRequest.getName())
                    .fact(itemRequest.getFact())
                    .isDef(itemRequest.getIsDef())
                    .build();

            unit.addUnitItem(unitItem);
        }

        // Set default unit item if not specified
        setDefaultUnitItemIfNeeded(unit);

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

        // Only update unit items if they are provided in the request
        if (request.getUnitItems() != null && !request.getUnitItems().isEmpty()) {
            validateAndUpdateUnitItems(unit, request.getUnitItems());
        }

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

//        if (!unit.getProductPrices().isEmpty()) {
//            unit.getProductPrices().forEach(price -> price.setPriceUnit(null));
//        }

        if (!unit.getProducts().isEmpty()) {
            unit.getProducts().forEach(product -> product.setDefaultUnit(null));
        }

        unitRepository.delete(unit);
    }


    private void validateUnitItemNames(List<? extends CreateUnitItemRequest> unitItems) {
        List<String> itemNames = unitItems.stream()
                .map(CreateUnitItemRequest::getName)
                .collect(Collectors.toList());

        // Check for duplicates in the request
        if (itemNames.size() != new HashSet<>(itemNames).size()) {
            throw new IllegalArgumentException("Unit item names must be unique within the same unit");
        }

        // Check against existing unit items in database
        if (unitRepository.existsByUnitItemsNameIn(itemNames)) {
            throw new IllegalArgumentException("One or more unit item names already exist");
        }
    }
    private void validateAndUpdateUnitItems(Unit unit, List<UpdateUnitItemRequest> unitItems) {
        // Validate unit item names uniqueness
        validateUnitItemNamesForUpdate(unit, unitItems);

        // Create a map of existing unit items by name for quick lookup
        Map<String, UnitItem> existingItemsMap = unit.getUnitItems().stream()
                .collect(Collectors.toMap(UnitItem::getName, Function.identity()));

        // Clear the current items collection (orphanRemoval will handle deletion)
        unit.getUnitItems().clear();

        // Process each item from the request
        for (UpdateUnitItemRequest itemRequest : unitItems) {
            UnitItem unitItem;

            // If item exists with this name, update it
            if (existingItemsMap.containsKey(itemRequest.getName())) {
                unitItem = existingItemsMap.get(itemRequest.getName());
                unitItem.setFact(itemRequest.getFact());
                unitItem.setIsDef(itemRequest.getIsDef());
            } else {
                // Create new item
                unitItem = UnitItem.builder()
                        .unit(unit)
                        .name(itemRequest.getName())
                        .fact(itemRequest.getFact())
                        .isDef(itemRequest.getIsDef())
                        .build();
            }

            unit.addUnitItem(unitItem);
        }

        // Set default unit item if needed
        setDefaultUnitItemIfNeeded(unit);
    }
    private void validateUnitItemNamesForUpdate(Unit unit, List<UpdateUnitItemRequest> unitItems) {
        List<String> newNames = unitItems.stream()
                .map(UpdateUnitItemRequest::getName)
                .collect(Collectors.toList());

        // Check for duplicates in the request
        if (newNames.size() != new HashSet<>(newNames).size()) {
            throw new IllegalArgumentException("Unit item names must be unique within the same unit");
        }

        // Get names of existing items in this unit
        Set<String> existingNamesInThisUnit = unit.getUnitItems().stream()
                .map(UnitItem::getName)
                .collect(Collectors.toSet());

        // Check against other unit items in database (only for new names)
        List<String> namesToCheck = newNames.stream()
                .filter(name -> !existingNamesInThisUnit.contains(name))
                .collect(Collectors.toList());

        if (!namesToCheck.isEmpty() && unitRepository.existsByUnitItemsNameInAndIdNot(namesToCheck, unit.getId())) {
            throw new IllegalArgumentException("One or more unit item names already exist in other units");
        }
    }

    private void updateUnitItems(Unit unit, List<UpdateUnitItemRequest> unitItems) {
        // Clear existing items (cascade will handle the deletion)
        unit.getUnitItems().clear();

        // Add new items
        for (UpdateUnitItemRequest itemRequest : unitItems) {
            UnitItem unitItem = UnitItem.builder()
                    .unit(unit)
                    .name(itemRequest.getName())
                    .fact(itemRequest.getFact())
                    .isDef(itemRequest.getIsDef())
                    .build();

            unit.addUnitItem(unitItem);
        }
    }

    private void setDefaultUnitItemIfNeeded(Unit unit) {
        if (unit.getUnitItems() != null && !unit.getUnitItems().isEmpty()) {
            // Check if any item is marked as default
            boolean hasDefault = unit.getUnitItems().stream()
                    .anyMatch(item -> item.getIsDef() != null && item.getIsDef());

            // If no default item, set the first one as default
            if (!hasDefault) {
                unit.getUnitItems().get(0).setIsDef(true);
            }
        }
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
