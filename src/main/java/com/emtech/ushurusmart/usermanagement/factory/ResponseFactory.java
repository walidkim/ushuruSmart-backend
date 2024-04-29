package com.emtech.ushurusmart.usermanagement.factory;

import com.emtech.ushurusmart.usermanagement.Dtos.controller.RequestDtos;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;

public class ResponseFactory {
    public static RequestDtos.UserResponse createOwnerResponse(Owner data) {
        RequestDtos.UserResponse result = new RequestDtos.UserResponse();
        result.setId(data.getId());
        result.setName(data.getName());
        result.setEmail(data.getEmail());
        result.setPhoneNumber(data.getPhoneNumber());
        return result;
    }

    public static RequestDtos.UserResponse createAssistantResponse(Assistant data) {

        RequestDtos.UserResponse result = new RequestDtos.UserResponse();
        result.setId(data.getId());
        result.setName(data.getName());
        result.setEmail(data.getEmail());
        result.setPhoneNumber(data.getPhoneNumber());
        return result;
    }
}
