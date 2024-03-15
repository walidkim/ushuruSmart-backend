package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.usermanagement.model.Etims;
import com.emtech.ushurusmart.usermanagement.repository.EtimsRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private EtimsRepository etimsRepository;

    public Etims findByBusinessKRAPin(String businessKRAPin) {
        Optional<Etims> etims = etimsRepository.findByBusinessKRAPin(businessKRAPin);
        if (etims.isPresent()) {
            return etims.get();
        } else
            return null;
    }

    public Etims etimsSave(Etims data) {
        return etimsRepository.save(data);

    }

    public Etims etimsUpdate(Etims data) {
        if (!etimsRepository.findByBusinessOwnerKRAPin(data.getBusinessOwnerKRAPin()).isPresent()) {
            return null;
        }
        return etimsRepository.save(data);
    }

    public boolean etimsDelete(String businessOwnerKRAPin) {
        try {

            if (!etimsRepository.findByBusinessKRAPin(businessOwnerKRAPin).isPresent()) {
                return false;
            }
            etimsRepository.deleteById(Long.valueOf(businessOwnerKRAPin));
            return true;

        } catch (Exception e) {
            return false;
        } finally {
            return false;
        }

    }

    public Etims findByBusinessOwnerKRAPin(String businessOwnerKRAPin) {
        Optional<Etims> result = etimsRepository.findByBusinessOwnerKRAPin(businessOwnerKRAPin);
        if (!result.isPresent()) {
            return result.get();
        }
        return null;

    }
}