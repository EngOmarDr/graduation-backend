package com.graduationProject._thYear.Auth.dtos.response;

import com.graduationProject._thYear.Auth.models.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private Role role;
    private Integer branchId;

}
