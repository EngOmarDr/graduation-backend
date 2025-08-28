package com.graduationProject._thYear.Auth.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    USER,
    ADMIN,
    MANAGER,

    PURCHASE_MANAGER;



    @JsonCreator
    public static Role fromString(String value) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + value);
    }

}
