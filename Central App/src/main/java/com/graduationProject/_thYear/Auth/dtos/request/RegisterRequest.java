package com.graduationProject._thYear.Auth.dtos.request;

import com.graduationProject._thYear.Auth.models.Role;
import jakarta.validation.constraints.NotBlank;
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
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    private String firstname;

    @NotBlank(message = "Last name is required")
    private String lastname;

    @NotBlank(message = "Username is required")
    @Pattern(regexp = "[a-zA-Z0-9]{4,}",message = "Username should be valid")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private Integer branchId;

    @NotBlank(message = "Role is required")
    private String  role;

}
