package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.etims_middleware.EtimsMiddleware;
import com.emtech.ushurusmart.usermanagement.Dtos.OwnerDto;
import com.emtech.ushurusmart.usermanagement.controller.HelperUtil;
import com.emtech.ushurusmart.usermanagement.factory.EntityFactory;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import com.emtech.ushurusmart.utils.controller.ResContructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {
    @Autowired
    private OwnerRepository ownerRepository;


    @Autowired
    private EtimsMiddleware etimsMiddleware;


    @Autowired
    private PasswordEncoder passwordEncoder;


    public Owner findByEmail(String email) {
        return ownerRepository.findByEmail(email);
    }

    public Owner save(Owner owner) {
        owner.setPassword(passwordEncoder.encode(owner.getPassword()));
        return ownerRepository.save(owner);
    }


    public ResponseEntity<ResContructor> validateAndCreateUser(String type, OwnerDto data, ResContructor res) {
        if (findByEmail(data.getEmail()) != null) {
            res.setMessage(HelperUtil.capitalizeFirst(type) + " with that email exists!");
            return ResponseEntity.badRequest().body(res);
        }
        ResponseEntity<?> response= etimsMiddleware.verifyBusinessKRAPin(data.getBusinessKRAPin());
        System.out.println(response);
        if(response.getStatusCode()==HttpStatus.FOUND){
            Owner owner = EntityFactory.createOwner(data);
            res.setMessage(HelperUtil.capitalizeFirst(type) + " created successfully!");
            res.setData(save(owner));
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }
        else{
            res.setMessage(HelperUtil.capitalizeFirst(type) + " can not be onboarded. Business is not registered by KRA!");
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(res);
        }
    }


    public Owner deleteByEmail(String email) {
        return ownerRepository.deleteByEmail(email);
    }

    public Owner findByPhoneNumber(String phoneNumber) {
        return ownerRepository.findByPhoneNumber(phoneNumber);
    }
}
