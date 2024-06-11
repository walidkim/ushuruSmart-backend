package com.emtech.ushurusmart.usermanagement.Dtos.controller;

import lombok.Data;

@Data
public class UpdateProfileDto {
    private String name;
    private String email;
    private byte[] profilePhoto;
}
