package com.emtech.ushurusmart.utils.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/otp")
public class otpController {

    @Autowired
    otpRepository otpRepository;
    OTPService otpservice;

    public otpController(OTPService otpService) {
        this.otpservice = otpService;
    }

    @GetMapping("/genotp")
    public String generateOTP() {
        return otpservice.generateOTP();
    }

    @GetMapping("/sendotp")
    public String sendotp(@RequestBody otpEntity sendOTPRequest) {

        return otpservice.sendOTP(sendOTPRequest.getUsertag(), sendOTPRequest.getphoneNo());
    }

    @GetMapping(path = "/verifyotp")
    public boolean verifyOTP(@RequestBody otpEntity verifyOTPRequest) {
        return otpservice.verifyOTP(verifyOTPRequest.getUsertag(), verifyOTPRequest.getOtpcode());
    }
}
