package com.emtech.ushurusmart.payments.service;

import com.emtech.ushurusmart.payments.callback.StkCallbackRequest;
import org.springframework.http.ResponseEntity;

public interface DarajaApi {
    String getAccessToken();
    //STKRequestResponse sendSTK(String phoneNo, int amount);
    ResponseEntity<String> sendSTK(String phoneNo, int amount);

    void callback(StkCallbackRequest stkCallbackRequest) throws Exception;

}
