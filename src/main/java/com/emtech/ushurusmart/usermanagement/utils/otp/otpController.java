package com.walid.myApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walid.myApp.otpEntity;
import com.walid.myApp.Repositoy.otpRepository;
import com.walid.myApp.service.OTPService;

@RestController
@RequestMapping("api")
public class myApp {

    @Autowired
    otpRepository otpRepository;
    OTPService otpservice;

    public myApp(OTPService otpService) {
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
