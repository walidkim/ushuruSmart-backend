//package com.emtech.ushurusmart.usermanagement.service;
//
//import com.emtech.ushurusmart.Etims.service.AdminService;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Objects;
//import java.util.regex.Pattern;
//
//@Service
//public class KraPINValidator {
//
//    @Autowired
//    private AdminService adminService;
//
//    public boolean validateKRAPins(String businessKRAPin, String businessOwnerKRAPin) {
//        Objects.requireNonNull(businessKRAPin, "businessKRAPin cannot be null");
//        Objects.requireNonNull(businessOwnerKRAPin, "businessOwnerKRAPin cannot be null");
//
//        Pattern kraPinPattern = Pattern.compile("^[A-Z]\\d{9}[A-Z]$");
//        if (!kraPinPattern.matcher(businessKRAPin).matches()) {
//            return false;
//        }
//
//        if (!kraPinPattern.matcher(businessOwnerKRAPin).matches()) {
//            return false;
//        }
//        if (adminService.findByBusinessKRAPin(businessKRAPin) == null) {
//            return false;
//        }
//        if (adminService.findByBusinessOwnerKRAPin(businessOwnerKRAPin) == null) {
//            return false;
//        }
//        return true;
//    }
//}