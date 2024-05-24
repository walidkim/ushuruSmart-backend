package com.emtech.ushurusmart.usermanagement.Dtos.controller;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String oldPassWord;
    private String newPassWord;
    private String confirmPassword;

}
