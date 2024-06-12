package com.emtech.ushurusmart.usermanagement.Dtos.controller;

import lombok.Data;

public class RequestDtos {
    @Data
    public static  class UserResponse{
        private long id;
        private String name;
        private String email;
        private String phoneNumber;

    }
}
