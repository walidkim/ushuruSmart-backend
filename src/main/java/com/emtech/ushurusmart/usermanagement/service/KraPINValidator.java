package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.usermanagement.repository.EtimsRepository;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class KraPINValidator {

    private final OwnerRepository ownerRepository;
    private final EtimsRepository etimsRepository;

    public KraPINValidator(OwnerRepository ownerRepository, EtimsRepository etimsRepository) {
        this.ownerRepository = ownerRepository;
        this.etimsRepository = etimsRepository;
    }

    public void validateKRAPins(String businessKRAPin, String businessOwnerKRAPin) {
        Objects.requireNonNull(businessKRAPin, "businessKRAPin cannot be null");
        Objects.requireNonNull(businessOwnerKRAPin, "businessOwnerKRAPin cannot be null");

        Pattern kraPinPattern = Pattern.compile("^[A-Z]\\d{10}[A-Z]$");

        if (!kraPinPattern.matcher(businessKRAPin).matches()) {
            throw new IllegalArgumentException("The business KRA PIN must be in the format 'A0101010101B'");
        }
        if (!kraPinPattern.matcher(businessOwnerKRAPin).matches()) {
            throw new IllegalArgumentException("The business owner KRA PIN must be in the format 'A0101010101B'");
        }
        if (!etimsRepository.findByBusinessKRAPin(businessKRAPin)) {
            throw new IllegalArgumentException("The business KRA PIN does not exist in the admin database");
        }
        if (!etimsRepository.findByBusinessOwnerKRAPin(businessOwnerKRAPin)) {
            throw new IllegalArgumentException("The business owner KRA PIN does not exist in the admin database");
        }
    }
}