package com.graduationProject._thYear.Warehouse.services;

import com.graduationProject._thYear.Branch.models.Branch;
import com.graduationProject._thYear.Branch.repositories.BranchRepository;
import com.graduationProject._thYear.Branch.services.BranchService;
import com.graduationProject._thYear.EventSyncronization.Records.WarehouseRecord;
import com.graduationProject._thYear.Warehouse.dtos.requests.CreateWarehouseRequest;
import com.graduationProject._thYear.Warehouse.dtos.requests.UpdateWarehouseRequest;
import com.graduationProject._thYear.Warehouse.dtos.responses.WarehouseResponse;
import com.graduationProject._thYear.Warehouse.dtos.responses.WarehouseStockResponse;
import com.graduationProject._thYear.Warehouse.dtos.responses.WarehouseTreeResponse;
import com.graduationProject._thYear.Warehouse.dtos.responses.WarehouseStockResponse.WarehouseStockItem;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import com.graduationProject._thYear.Warehouse.models.WarehouseType;
import com.graduationProject._thYear.Warehouse.repositories.WarehouseRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService{

    private final WarehouseRepository warehouseRepository;
    private final BranchRepository branchRepository;
    private final BranchService branchService;

    @Override
    @Transactional
    public WarehouseResponse createWarehouse(CreateWarehouseRequest request) {

        //Validate name and code and phone uniqueness
        if (warehouseRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Warehouse item with code '" + request.getCode() + "' already exists");
        }
        if (warehouseRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Warehouse item with name '" + request.getName() + "' already exists");
        }



        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        WarehouseType warehouseType;
        try {
            warehouseType = WarehouseType.valueOf(String.valueOf(request.getType()).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + request.getType());
        }

        Warehouse warehouse = Warehouse.builder()
                .name(request.getName())
                .code(request.getCode())
                .phone(request.getPhone())
                .address(request.getAddress())
                .branch(branch)
                .type(warehouseType)
                .isActive(request.isActive())
                .notes(request.getNotes())
                .build();

        if (request.getParentId() != null) {
            Warehouse parent = warehouseRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent warehouse not found"));
            warehouse.setParent(parent);
        }else {
            warehouse.setParent(null);
        }

        Warehouse saved = warehouseRepository.save(warehouse);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public WarehouseResponse updateWarehouse(Integer id, UpdateWarehouseRequest request) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found"));

        if (request.getCode() != null && !warehouse.getCode().equals(request.getCode())) {
            if (warehouseRepository.existsByCode(request.getCode())) {
                throw new IllegalArgumentException("Warehouse with code '" + request.getCode() + "' already exists");
            }
            warehouse.setCode(request.getCode());
        }

        if (request.getName() != null && !warehouse.getName().equals(request.getName())) {
            if (warehouseRepository.existsByName(request.getName())) {
                throw new IllegalArgumentException("Warehouse with name '" + request.getName() + "' already exists");
            }
            warehouse.setName(request.getName());
        }

        if (request.getPhone() != null) {
            warehouse.setPhone(request.getPhone());
        }

        if (request.getAddress() != null) {
            warehouse.setAddress(request.getAddress());
        }

        if (request.getBranchId() != null) {
            Branch branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new EntityNotFoundException("Branch not found"));
            warehouse.setBranch(branch);
        }

        if (request.getType() != null) {
            try {
                WarehouseType warehouseType = WarehouseType.valueOf(request.getType().toString().toUpperCase());
                warehouse.setType(warehouseType);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid warehouse type: " + request.getType());
            }
        }

        if (request.getNotes() != null) {
            warehouse.setNotes(request.getNotes());
        }

        warehouse.setActive(request.isActive());

        if (request.getParentId() != null) {
            Warehouse parent = warehouseRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent warehouse not found"));
            warehouse.setParent(parent);
        } else {
            warehouse.setParent(null); // Explicitly set to null if not provided
        }

        Warehouse updated = warehouseRepository.save(warehouse);
        return mapToDTO(updated);
    }

    @Override
    public void deleteWarehouse(Integer id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found"));
        warehouseRepository.delete(warehouse);
    }

    @Override
    public WarehouseResponse getWarehouseById(Integer id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found"));
        return mapToDTO(warehouse);
    }

    @Override
    public List<WarehouseResponse> getAllWarehouses() {
        return warehouseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WarehouseTreeResponse> getWarehouseTree() {
        List<Warehouse> rootWarehouse = warehouseRepository.findByParentIsNull();
        return rootWarehouse.stream()
                .map(this::convertToTreeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WarehouseResponse> getChildWarehouses(Integer parentId) {
        return warehouseRepository.findByParentId(parentId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WarehouseResponse> searchWarehouse(String searchTerm) {
        return warehouseRepository.searchByNameOrCode(searchTerm).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    
    public WarehouseStockResponse getStock(Integer warehouseId, Integer productId, Integer groupId){
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse with id "+ warehouseId +" not found."));

        List<WarehouseStockItem> items = warehouseRepository.getStock(warehouseId, productId, groupId).stream()
            .map((Tuple item) -> WarehouseStockItem.fromTuple(item))
            .collect(Collectors.toList());

        WarehouseStockResponse response =  WarehouseStockResponse.builder()
            .warehouseId(warehouse.getId())
            .warehouseName(warehouse.getName())
            .items(items)
            .build();
            
        return response;
    }

    @Override
    public Warehouse saveOrUpdate(WarehouseRecord warehouseRecord){
        if (warehouseRecord == null){
            return null;
        }
        Warehouse warehouse = warehouseRepository.findByGlobalId(warehouseRecord.getGlobalId())
            .orElse(new Warehouse());

        Warehouse parent = saveOrUpdate(warehouseRecord.getParent());

        Branch branch = branchService.saveOrUpdate(warehouseRecord.getBranch());
        
        warehouse = warehouse.toBuilder()
            .globalId(warehouseRecord.getGlobalId())
            .name(warehouseRecord.getName())
            .code(warehouseRecord.getCode())
            .phone(warehouseRecord.getPhone())
            .type(warehouseRecord.getType())
            .isActive(warehouseRecord.isActive())
            .address(warehouseRecord.getAddress())
            .notes(warehouseRecord.getNotes())
            .parent(parent)
            .branch(branch)
            .build();
            
        warehouseRepository.save(warehouse);
        return warehouse;
    }
    private WarehouseResponse mapToDTO(Warehouse warehouse) {
        return WarehouseResponse.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .code(warehouse.getCode())
                .phone(warehouse.getPhone())
                .address(warehouse.getAddress())
                .parentId(warehouse.getParent() != null ? warehouse.getParent().getId() : null)
                .warehouseParentName(warehouse.getParent() != null ? warehouse.getParent().getName() : null)
                .branchId(warehouse.getBranch() != null ? warehouse.getBranch().getId() : null)
                .branchName(warehouse.getBranch() != null ? warehouse.getBranch().getName() : null)
                .type(warehouse.getType())
                .isActive(warehouse.isActive())
                .notes(warehouse.getNotes())
                .build();
    }

    private WarehouseTreeResponse convertToTreeResponse(Warehouse warehouse) {
        return WarehouseTreeResponse.builder()
                .id(warehouse.getId())
                .code(warehouse.getCode())
                .name(warehouse.getName())
                .phone(warehouse.getPhone())
                .address(warehouse.getAddress())
                .parentId(warehouse.getParent() != null ? warehouse.getParent().getId() : null)
                .warehouseParentName(warehouse.getParent() != null ? warehouse.getParent().getName() : null)
                .branchId(warehouse.getBranch() != null ? warehouse.getBranch().getId() : null)
                .branchName(warehouse.getBranch() != null ? warehouse.getBranch().getName() : null)
                .type(warehouse.getType())
                .isActive(warehouse.isActive())
                .notes(warehouse.getNotes())
                .children(warehouse.getChildren().stream()
                        .map(this::convertToTreeResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}
