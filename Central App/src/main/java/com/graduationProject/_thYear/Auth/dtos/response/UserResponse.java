package com.graduationProject._thYear.Auth.dtos.response;

import java.util.Optional;

import com.graduationProject._thYear.Auth.models.Role;
import com.graduationProject._thYear.Auth.models.User;
import com.graduationProject._thYear.Warehouse.models.Warehouse;

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
    private Integer warehouseId;


    public static UserResponse fromUserEntity(User user){
        return UserResponse.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .username(user.getUsername())
            .role(user.getRole())
            .warehouseId(Optional.ofNullable(user.getWarehouse())
                .map(warehouse -> warehouse.getId())
                .orElse(null)
            )
            .build();
    }
}
