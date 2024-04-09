package com.emtech.ushurusmart.usermanagement.Dtos.auth;


import lombok.Data;

@Data
public class OtpDataDto {
    String phoneNumber;
    String type;
    String optCode;
}
