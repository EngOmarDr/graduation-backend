package com.graduationProject._thYear.Auth.dtos.request;

import com.graduationProject._thYear.Auth.models.Role;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    @Size(min = 2, max = 50)
    private String firstName;

    @Size(min = 2, max = 50)
    private String lastName;

    @Pattern(regexp = "[a-zA-Z0-9]{4,}", message = "Username should be alphanumeric with at least 4 characters")
    private String username;

    private Role role;
    private Integer branchId;
}
