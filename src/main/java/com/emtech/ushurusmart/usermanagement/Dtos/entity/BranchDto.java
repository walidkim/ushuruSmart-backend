package com.emtech.ushurusmart.usermanagement.Dtos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BranchDto {
    private String name;
    private String location;

    public BranchDto() {}

    public BranchDto(Long id, String name, String location) {
    }
}
