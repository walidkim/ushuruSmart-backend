package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {
    @Autowired
    private OwnerRepository ownerRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;


    public Owner findByEmail(String email) {
        return ownerRepository.findByEmail(email);
    }

    public Owner save(Owner owner) {
        owner.setPassword(passwordEncoder.encode(owner.getPassword()));
        return ownerRepository.save(owner);
    }

    public Owner deleteByEmail(String email) {
        return ownerRepository.deleteByEmail(email);
    }

    public Owner findByPhoneNumber(String phoneNumber) {
        return ownerRepository.findByPhoneNumber(phoneNumber);
    }
}
