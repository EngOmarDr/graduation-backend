package com.graduationProject._thYear.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class CategoryResponse {
    private Integer id;
    private String code;
    private String name;
    private String notes;
    private Integer parentId;
    private String parentName;
}
