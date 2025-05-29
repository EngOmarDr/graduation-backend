package com.graduationProject._thYear.Branch.services;

import com.graduationProject._thYear.Branch.dtos.request.CreateBranchRequest;
import com.graduationProject._thYear.Branch.dtos.request.UpdateBranchRequest;
import com.graduationProject._thYear.Branch.dtos.response.BranchResponse;

import java.util.List;

public interface BranchService {
    BranchResponse createBranch(CreateBranchRequest request);

    BranchResponse getBranchById(Integer id);

    List<BranchResponse> getAllBranchs();

    BranchResponse updateBranch(Integer id, UpdateBranchRequest request);

    void deleteBranch(Integer id);
}
