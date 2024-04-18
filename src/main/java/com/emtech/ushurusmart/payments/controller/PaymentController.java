package com.emtech.ushurusmart.payments.controller;

import com.emtech.ushurusmart.payments.dtos.callback.StkCallbackRequest;
import com.emtech.ushurusmart.payments.dtos.sendPushRequest;
import com.emtech.ushurusmart.payments.service.PaymentImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("mpesa")
public class PaymentController {

    private final PaymentImpl darajaApiImpl=new PaymentImpl();

    @PostMapping("/sendPush")
    public ResponseEntity<String> sendSTK(@RequestBody sendPushRequest request) {
        int amountdue;
        String phoneNo = request.getPhoneNo().trim();
        String amount = String.valueOf(request.getAmount());
        String regex = "^[0-9]{9}";
        String regex2 = "^[0-9]{1,6}$";

        // *************Error checking for missing/blank json object

        if (phoneNo != null && amount != null && !phoneNo.isEmpty() && !amount.isEmpty() &&
                phoneNo.matches(regex) && amount.matches(regex2)) {
            amountdue = Integer.parseInt(amount);
            if (amountdue <= 150000 && amountdue > 0) {
                return darajaApiImpl.sendSTK(phoneNo, amountdue);
            } else {
                return ResponseEntity.badRequest()
                        .body("Invalid Phone Number or Amount,Amount should be less than KES150,000");
            }

        } else {
            return ResponseEntity.badRequest()
                    .body("Provide valid phone number and amount.");
        }

    }

    @PostMapping("/callback")
    public void callback (@RequestBody StkCallbackRequest stkCallbackRequest) throws Exception {
        darajaApiImpl.callback(stkCallbackRequest);
    }

}


