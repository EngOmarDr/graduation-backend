package com.graduationProject._thYear.Branch.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBranchRequest {

    @NotNull(message = "name is required")
    private String name;

    @NotNull(message = "phone is required")
    private String phone;

    private String address;

    private String notes;

}
