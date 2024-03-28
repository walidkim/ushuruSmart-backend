package com.emtech.ushurusmart.usermanagement.Dtos;

import lombok.Data;

@Data
public class OwnerDto {
    private String businessKRAPin;
    private String businessOwnerKRAPin;
    private String email;
    private String name;
    private String password;
    private String phoneNumber;
}
