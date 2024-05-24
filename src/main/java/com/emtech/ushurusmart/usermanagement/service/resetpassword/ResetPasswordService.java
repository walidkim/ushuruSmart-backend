package com.emtech.ushurusmart.usermanagement.service.resetpassword;

import com.emtech.ushurusmart.usermanagement.Dtos.controller.ResetPasswordRequest;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.repository.AssistantRepository;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordService {

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordService.class);
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private AssistantRepository assistantRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void changePassword(String email, ResetPasswordRequest resetPasswordRequest, String userType) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = userDetails.getUsername();
        logger.info("attempting to find user with email: {}", userEmail);
        if ("owner".equalsIgnoreCase(userType)) {
            Owner owner = ownerRepository.findByEmail(userEmail.trim());

            if (owner == null) {
                logger.error("user not found with email: {}", email);
                throw new BadCredentialsException("User not found");
            }
            handlePasswordChange(owner.getPassword(), resetPasswordRequest, email);
            owner.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassWord()));
            ownerRepository.save(owner);
        } else if ("Assistant".equalsIgnoreCase(userType)) {
            Assistant assistant = assistantRepository.findByEmail(userEmail.trim());
            if (assistant == null) {
                throw new BadCredentialsException("User not Found");
            }
            handlePasswordChange(assistant.getPassword(), resetPasswordRequest, email);
            assistant.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassWord()));
            assistantRepository.save(assistant);
        }
        logger.info("Password successfully changed for user: {}", email);

    }
    private void handlePasswordChange(String currentPassword, ResetPasswordRequest resetPasswordRequest, String email) {
        if (!passwordEncoder.matches(resetPasswordRequest.getOldPassWord(), currentPassword)) {
            String errorMessage = String.format("Old password is incorrect for user: %s", email);
            logger.error(errorMessage);
            throw new BadCredentialsException(errorMessage);
        }
        if (!isPasswordMatch(resetPasswordRequest.getNewPassWord(), resetPasswordRequest.getConfirmPassword())) {
            String errorMessage = String.format("New Password and Confirm Password do not match for user: %s", email);
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private boolean isPasswordMatch(String newPassword, String confirmPassword) {
        return newPassword != null && newPassword.contentEquals(confirmPassword);
    }
}


