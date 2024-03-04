package com.emtech.ushurusmart.usermanagement.Dtos;

import lombok.Data;

@Data
public class SignUpReq {
    private String name;
    private String email;
    private String password;

}
