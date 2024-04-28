package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.config.LoggerSingleton;
import com.emtech.ushurusmart.etims_middleware.EtimsMiddleware;
import com.emtech.ushurusmart.usermanagement.Dtos.LoginRequest;
import com.emtech.ushurusmart.usermanagement.Dtos.OwnerDto;
import com.emtech.ushurusmart.usermanagement.Dtos.controller.RequestDtos;
import com.emtech.ushurusmart.usermanagement.controller.HelperUtil;
import com.emtech.ushurusmart.usermanagement.factory.EntityFactory;
import com.emtech.ushurusmart.usermanagement.factory.ResponseFactory;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import com.emtech.ushurusmart.utils.controller.ResContructor;
import com.emtech.ushurusmart.utils.otp.OTPService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class OwnerService extends LoggerSingleton {
    @Autowired
    private OwnerRepository ownerRepository;


    @Autowired
    private EtimsMiddleware etimsMiddleware;


    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private OTPService otpService;

    public Owner findByEmail(String email) {
        return ownerRepository.findByEmail(email);
    }

    public Owner save(Owner owner) {
        owner.setPassword(passwordEncoder.encode(owner.getPassword()));
        return ownerRepository.save(owner);
    }


    public ResponseEntity<ResContructor> validateAndCreateOwner(String type, OwnerDto data, ResContructor res) {
        if (findByEmail(data.getEmail()) != null) {
            res.setMessage(HelperUtil.capitalizeFirst(type) + " with that email exists!");
            return ResponseEntity.badRequest().body(res);
        }
        ResponseEntity<?> response= etimsMiddleware.verifyBusinessKRAPin(data.getBusinessKRAPin());
        if(response.getStatusCode()==HttpStatus.FOUND){
            System.out.println(res.toString());
            Owner owner = EntityFactory.createOwner(data);

            res.setMessage(HelperUtil.capitalizeFirst(type) + " created successfully!");
            save(owner);
            RequestDtos.OwnerResponse resOwner= ResponseFactory.createOwnerResponse(owner);
            logger.info(resOwner.toString());
            res.setData(resOwner);
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }
        else{

            res.setMessage(HelperUtil.capitalizeFirst(type) + " can not be onboarded. Business is not registered by KRA!");
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(res);
        }
    }


    public ResponseEntity<ResContructor> loginOwner(@NotNull String type, LoginRequest loginReq, ResContructor res) throws Exception {
       try {
           Owner owner = findByEmail(loginReq.getEmail());
           if (owner == null) {
                throw new BadCredentialsException("Invalid email or password");
           }

           Authentication authentication = authenticationManager
                   .authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(),
                           loginReq.getPassword(),owner.getAuthorities() != null ? owner.getAuthorities() : Collections.emptyList()));
           otpService.sendOTP(owner.getPhoneNumber());
           res.setMessage("A short code has been sent to your phone for verification");
           Map<String,String> resBody= new HashMap<>();
           resBody.put("type", type);
           resBody.put("phoneNumber", owner.getPhoneNumber());
           res.setData(resBody);
           return ResponseEntity.status(HttpStatus.CREATED).body(res);
       }

         catch (BadCredentialsException e) {
            res.setMessage("Invalid email or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
    }

    public Owner deleteByEmail(String email) {
        return ownerRepository.deleteByEmail(email);
    }

    public Owner findByPhoneNumber(String phoneNumber) {
        return ownerRepository.findByPhoneNumber(phoneNumber);
    }


}
