package com.emtech.ushurusmart.usermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class KraPINValidator {

    @Autowired
    private AdminService adminService;

    public boolean validateKRAPins(String businesskrapin, String businessOwnerKRAPin) {
        Objects.requireNonNull(businesskrapin, "businesskrapin cannot be null");
        Objects.requireNonNull(businessOwnerKRAPin, "businessOwnerKRAPin cannot be null");

        Pattern kraPinPattern = Pattern.compile("^[A-Z]\\d{9}[A-Z]$");

        if (!kraPinPattern.matcher(businesskrapin).matches()) {
            System.out.println("business does not match");
            return false;
        }
        if (!kraPinPattern.matcher(businessOwnerKRAPin).matches()) {
            System.out.println("owner does not match");
            return false;
        }
        if (adminService.findBybusinesskrapin(businesskrapin) == null) {

            System.out.println("business does not exist" + businesskrapin + businessOwnerKRAPin);
            return false;
        }
        if (adminService.findByBusinessOwnerKRAPin(businessOwnerKRAPin) == null) {
            System.out.println("owner does not exist");
            return false;
        }
        return true;
    }
}