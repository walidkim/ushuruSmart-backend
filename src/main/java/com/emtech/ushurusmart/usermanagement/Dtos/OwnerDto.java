package com.emtech.ushurusmart.usermanagement.Dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OwnerDto {

    @NotNull(message = "businessKRAPin cannot be null")
    private String businessKRAPin;

    @NotNull(message = "businessOwnerKRAPin cannot be null")
    private String businessOwnerKRAPin;

    @NotNull(message = "email cannot be null")
    private String email;

    @NotNull(message = "name cannot be null")
    private String name;

    @NotNull(message = "password cannot be null")
    private String password;

    @NotNull(message = "phoneNumber cannot be null")
    private String phoneNumber;
}
