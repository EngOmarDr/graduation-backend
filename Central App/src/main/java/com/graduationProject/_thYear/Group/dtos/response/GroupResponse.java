package com.graduationProject._thYear.Group.dtos.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class GroupResponse {
    private Integer id;
    private String code;
    private String name;
    private String notes;
    private Integer parentId;
    private String parentName;
}
