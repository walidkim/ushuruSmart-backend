package com.emtech.ushurusmart.usermanagement.factory;

import com.emtech.ushurusmart.usermanagement.Dtos.controller.RequestDtos.OwnerResponse;
import com.emtech.ushurusmart.usermanagement.model.Owner;

public class ResponseFactory {
    public static OwnerResponse createOwnerResponse(Owner data) {
        OwnerResponse result = new OwnerResponse();
        result.setId(data.getId());
        result.setName(data.getName());
        result.setEmail(data.getEmail());
        result.setPhoneNumber(data.getPhoneNumber());
        return result;
    }
}
