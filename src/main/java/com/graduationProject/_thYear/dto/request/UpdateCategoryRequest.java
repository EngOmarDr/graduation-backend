package com.graduationProject._thYear.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor  // Ensure this is present
@AllArgsConstructor
@SuperBuilder
public class UpdateCategoryRequest {
    @NotNull(message = "Code is required")
    private String code;

    @NotNull(message = "Name is required")
    private String name;

    private String notes;
    private Integer parentId;
}
