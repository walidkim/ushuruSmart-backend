package com.emtech.ushurusmart.usermanagement.Dtos.auth;


import lombok.Data;

@Data
public class OwnerSignUp {
    private String businessKRAPin;
    private String businessOwnerKRAPin;
    private String email;
    private String name;
    private String password;
    private String phoneNumber;
}
