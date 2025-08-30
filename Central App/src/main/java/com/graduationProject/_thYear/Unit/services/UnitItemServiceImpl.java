package com.graduationProject._thYear.Unit.services;

import com.graduationProject._thYear.EventSyncronization.Records.ProductRecord.GroupRecord;
import com.graduationProject._thYear.EventSyncronization.Records.ProductRecord.UnitItemRecord;
import com.graduationProject._thYear.Group.models.Group;
import com.graduationProject._thYear.Product.dtos.response.ProductBarcodeResponse;
import com.graduationProject._thYear.Product.models.ProductBarcode;
import com.graduationProject._thYear.Unit.dtos.requests.CreateUnitItemRequest;
import com.graduationProject._thYear.Unit.dtos.requests.UpdateUnitItemRequest;
import com.graduationProject._thYear.Unit.dtos.responses.UnitItemResponse;
import com.graduationProject._thYear.Unit.models.Unit;
import com.graduationProject._thYear.Unit.models.UnitItem;
import com.graduationProject._thYear.Unit.repositories.UnitItemRepository;
import com.graduationProject._thYear.Unit.repositories.UnitRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UnitItemServiceImpl implements UnitItemService{

    private final UnitItemRepository unitItemRepository;
    private final UnitRepository unitRepository;

    @Override
    public UnitItemResponse createUnitItem(CreateUnitItemRequest request) {
        // Validate name uniqueness
        if (unitItemRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Unit item with name '" + request.getName() + "' already exists");
        }

        Unit unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found with id: " + request.getUnitId()));

        UnitItem unitItem = new UnitItem();
        unitItem.setUnit(unit);
        unitItem.setName(request.getName());
        unitItem.setFact(request.getFact());
        unitItem.setIsDef(request.getIsDef());

        UnitItem savedUnitItem = unitItemRepository.save(unitItem);
        return convertToResponse(savedUnitItem);
    }

    @Override
    public UnitItemResponse getUnitItemById(Integer id) {
        UnitItem unitItem = unitItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UnitItem not found with id: " + id));
        return convertToResponse(unitItem);
    }

    @Override
    public List<UnitItemResponse> getAllUnitItems() {
        return unitItemRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UnitItemResponse> getUnitItemsByUnitId(Integer unitId) {
        return unitItemRepository.findByUnitId(unitId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UnitItemResponse updateUnitItem(Integer id, UpdateUnitItemRequest request) {
        UnitItem unitItem = unitItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UnitItem not found with id: " + id));

        if (request.getName() != null && !unitItem.getName().equals(request.getName())) {
            if (unitItemRepository.existsByName(request.getName())) {
                throw new IllegalArgumentException("Unit item with name '" + request.getName() + "' already exists");
            }
            unitItem.setName(request.getName());
        }

        if (request.getUnitId() != null) {
            Unit unit = unitRepository.findById(request.getUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unit not found with id: " + request.getUnitId()));
            unitItem.setUnit(unit);
        }

        if (request.getFact() != null) {
            unitItem.setFact(request.getFact());
        }

        if (request.getIsDef() != null) {
            unitItem.setIsDef(request.getIsDef());
        }

        UnitItem updatedUnitItem = unitItemRepository.save(unitItem);
        return convertToResponse(updatedUnitItem);
    }


    @Override
    public void deleteUnitItem(Integer id) {
        UnitItem unitItem = unitItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UnitItem not found with id: " + id));

        // Handle relationships before deletion
        if (!unitItem.getBarcodes().isEmpty()) {
            unitItem.getBarcodes().forEach(barcode -> barcode.setUnitItem(null));
        }

        unitItemRepository.delete(unitItem);
    }

    @Override
    public UnitItem saveOrUpdate(UnitItemRecord unitItemRecord){

        UnitItem unitItem = unitItemRepository.findByGlobalId(unitItemRecord.getGlobalId())
            .orElse(new UnitItem());

        unitItem.toBuilder()
            .globalId(unitItemRecord.getGlobalId())
            .name(unitItemRecord.getName())
            .fact(unitItemRecord.getFact())
            .isDef(unitItemRecord.getIsDef())
            .build();
            
        unitItemRepository.save(unitItem);
        return unitItem;
    }

    private UnitItemResponse convertToResponse(UnitItem unitItem) {
        return UnitItemResponse.builder()
                .id(unitItem.getId())
                .unitId(unitItem.getUnit().getId())
                .unitName(unitItem.getUnit().getName())
                .name(unitItem.getName())
                .fact(unitItem.getFact())
                .isDef(unitItem.getIsDef())
                .barcodes(convertBarcodesToResponse(unitItem.getBarcodes()))
                .build();
    }

    private List<ProductBarcodeResponse> convertBarcodesToResponse(List<ProductBarcode> barcodes) {
        return barcodes.stream()
                .map(this::convertBarcodeToResponse)
                .collect(Collectors.toList());
    }

    private ProductBarcodeResponse convertBarcodeToResponse(ProductBarcode barcode) {
        return ProductBarcodeResponse.builder()
                .id(barcode.getId())
                .productId(barcode.getProduct().getId())
                .productName(barcode.getProduct().getName())
                .unitItemId(barcode.getUnitItem().getId())
                .unitItemName(barcode.getUnitItem().getName())
                .barcode(barcode.getBarcode())
                .build();
    }

}
