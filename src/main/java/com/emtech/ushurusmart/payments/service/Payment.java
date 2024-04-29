package com.emtech.ushurusmart.payments.service;

import com.emtech.ushurusmart.payments.dtos.callback.StkCallbackRequest;
import org.springframework.http.ResponseEntity;

public interface Payment {
    String getAccessToken();
    //STKRequestResponse sendSTK(String phoneNo, int amount);
    ResponseEntity<String> sendSTK(String phoneNo, int amount);
    void callback(StkCallbackRequest stkCallbackRequest) throws Exception;

}
