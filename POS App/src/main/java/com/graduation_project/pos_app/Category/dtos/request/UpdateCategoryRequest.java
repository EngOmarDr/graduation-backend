package com.graduation_project.pos_app.Category.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
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
