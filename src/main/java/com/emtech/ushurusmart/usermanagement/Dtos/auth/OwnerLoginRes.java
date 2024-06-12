package com.emtech.ushurusmart.usermanagement.Dtos.auth;

import lombok.Data;

@Data
public class OwnerLoginRes {
    private Long id;
    private String name;
    private String email;
    private String businessKRAPin;
    private String businessOwnerKRAPin;
    private String phoneNumber;
}
