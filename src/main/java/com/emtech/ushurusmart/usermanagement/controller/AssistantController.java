package com.emtech.ushurusmart.usermanagement.controller;

import com.emtech.ushurusmart.usermanagement.Dtos.controller.ResetPasswordRequest;
import com.emtech.ushurusmart.usermanagement.service.resetpassword.ResetPasswordService;
import com.emtech.ushurusmart.utils.AuthUtils;
import com.emtech.ushurusmart.utils.controller.Responses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssistantController {
    @Autowired
    private ResetPasswordService resetPasswordService;

    @PostMapping("/Assistant/reset-password")
    public ResponseEntity<String> assistantPasswordReset(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            String email = AuthUtils.getCurrentlyLoggedInPerson();
            resetPasswordService.changePassword(email, resetPasswordRequest, "Assistant");
            return ResponseEntity.status(HttpStatus.OK).body("Password successfully changed for Assistant.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while resetting the password.");
        }
    }
}