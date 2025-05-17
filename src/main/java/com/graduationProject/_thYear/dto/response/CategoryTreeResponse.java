package com.graduationProject._thYear.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Setter
@Getter
@SuperBuilder
public class CategoryTreeResponse {
    private Integer id;
    private String code;
    private String name;
    private String notes;
    private List<CategoryTreeResponse> children;

}
