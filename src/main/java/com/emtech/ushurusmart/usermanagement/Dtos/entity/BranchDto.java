package com.emtech.ushurusmart.usermanagement.Dtos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BranchDto {
    private String name;
    private String location;
    private String supervisor;


    public BranchDto(String name, String location) {
        this.name = name;
        this.location = location;
    }
}
