package com.graduationProject._thYear.Branch.services;

import com.graduationProject._thYear.Branch.dtos.request.CreateBranchRequest;
import com.graduationProject._thYear.Branch.dtos.request.UpdateBranchRequest;
import com.graduationProject._thYear.Branch.dtos.response.BranchResponse;
import com.graduationProject._thYear.Branch.models.Branch;
import com.graduationProject._thYear.Branch.repositories.BranchRepository;
import com.graduationProject._thYear.exceptionHandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
    private final BranchRepository repository;

    @Override
    public BranchResponse createBranch(CreateBranchRequest request) {
        if (repository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Branch with name '" + request.getName() + "' already exists");
        }
        if (repository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Branch with phone '" + request.getPhone() + "' already exists");
        }

        var object = Branch.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .notes(request.getNotes())
                .build();

        var savedobject = repository.save(object);
        return convertToResponse(savedobject);
    }

    @Override
    public BranchResponse getBranchById(Integer id) {
        var res = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
        return convertToResponse(res);
    }

    @Override
    public List<BranchResponse> getAllBranchs() {
        return repository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BranchResponse updateBranch(Integer id, UpdateBranchRequest request) {
        var res = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));

        // Validate unique code and name if changed
        if (!res.getPhone().equals(request.getPhone()) &&
                repository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Branch with code '" + request.getPhone() + "' already exists");
        }
        if (!res.getName().equals(request.getName()) &&
                repository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Branch with name '" + request.getName() + "' already exists");
        }

        res.setName(request.getName());
        res.setPhone(request.getPhone());
        res.setAddress(request.getAddress());
        res.setNotes(request.getNotes());

        var updatedRes = repository.save(res);
        return convertToResponse(updatedRes);
    }

    @Override
    public void deleteBranch(Integer id) {
        var res = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));

        repository.delete(res);
    }

    private BranchResponse convertToResponse(Branch object) {
        return BranchResponse.builder()
                .id(object.getId())
                .name(object.getName())
                .phone(object.getPhone())
                .address(object.getAddress())
                .notes(object.getNotes())
                .build();
    }
}
