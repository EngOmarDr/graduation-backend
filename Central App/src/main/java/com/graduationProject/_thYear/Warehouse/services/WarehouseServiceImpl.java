package com.graduationProject._thYear.Warehouse.services;

import com.graduationProject._thYear.Auth.models.Role;
import com.graduationProject._thYear.Branch.models.Branch;
import com.graduationProject._thYear.Branch.repositories.BranchRepository;
import com.graduationProject._thYear.Group.dtos.response.GroupTreeResponse;
import com.graduationProject._thYear.Group.models.Group;
import com.graduationProject._thYear.Warehouse.dtos.requests.CreateWarehouseRequest;
import com.graduationProject._thYear.Warehouse.dtos.requests.UpdateWarehouseRequest;
import com.graduationProject._thYear.Warehouse.dtos.responses.WarehouseResponse;
import com.graduationProject._thYear.Warehouse.dtos.responses.WarehouseTreeResponse;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import com.graduationProject._thYear.Warehouse.models.WarehouseType;
import com.graduationProject._thYear.Warehouse.repositories.WarehouseRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
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
        if (warehouseRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Warehouse item with phone '" + request.getPhone() + "' already exists");
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

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch not found"));

        if (!warehouse.getCode().equals(request.getCode()) &&
                warehouseRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Group item with code '" + request.getCode() + "' already exists");
        }

        if (!warehouse.getName().equals(request.getName()) &&
                warehouseRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Group item with name '" + request.getName() + "' already exists");
        }
        if (!warehouse.getPhone().equals(request.getPhone()) &&
                warehouseRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Group item with phone '" + request.getPhone() + "' already exists");
        }

        WarehouseType warehouseType;
        try {
            warehouseType = WarehouseType.valueOf(String.valueOf(request.getType()).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + request.getType());
        }
        warehouse.setName(request.getName());
        warehouse.setCode(request.getCode());
        warehouse.setPhone(request.getPhone());
        warehouse.setAddress(request.getAddress());
        warehouse.setBranch(branch);
        warehouse.setType(warehouseType);
        warehouse.setActive(request.isActive());
        warehouse.setNotes(request.getNotes());

        if (request.getParentId() != null) {
            Warehouse parent = warehouseRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent warehouse not found"));
            warehouse.setParent(parent);
        }else {
            warehouse.setParent(null);
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
