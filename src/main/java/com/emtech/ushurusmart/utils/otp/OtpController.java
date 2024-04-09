package com.emtech.ushurusmart.utils.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/otp")
public class OtpController {

    @Autowired
    OTPService otpService;


//    @GetMapping("/genotp")
//    public String generateOTP() {
//        return otpservice.generateOTP();
//    }

    @GetMapping("/sendotp")
    public String sendotp(@RequestBody OtpEntity sendOTPRequest) {

        return otpService.sendOTP(sendOTPRequest.getUserTag());
    }

    @GetMapping(path = "/verifyotp")
    public boolean verifyOTP(@RequestBody OtpEntity verifyOTPRequest) {
        return otpService.verifyOTP(verifyOTPRequest.getUserTag(), verifyOTPRequest.getOtpCode());
    }
}
