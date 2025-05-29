package com.graduationProject._thYear.Branch.dtos.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class BranchResponse {
    private Integer id;
    private String name;
    private String phone;
    private String address;
    private String notes;

}
