package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.usermanagement.model.Etims;
import com.emtech.ushurusmart.usermanagement.repository.EtimsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private EtimsRepository etimsRepository;

    public Etims findBybusinesskrapin(String businesskrapin) {
        Optional<Etims> etims = etimsRepository.findByBusinessKraPin(businesskrapin);
        if (etims.isPresent()) {
            System.out.println("present");
            return etims.get();
        } else
            System.out.println(etims);
            return null;
    }

    public Etims etimsSave(Etims data) {
        return etimsRepository.save(data);

    }

    public Etims etimsUpdate(Etims data) {
        if (!etimsRepository.findByBusinessOwnerKraPin(data.getBusinessOwnerKraPin()).isPresent()) {
            return null;
        }
        return etimsRepository.save(data);
    }

    public boolean etimsDelete(String businessOwnerKRAPin) {
        try {

            if (!etimsRepository.findByBusinessKraPin(businessOwnerKRAPin).isPresent()) {
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
        Optional<Etims> result = etimsRepository.findByBusinessOwnerKraPin(businessOwnerKRAPin);

        if (result.isPresent()) {
            System.out.println("present");
            return result.get();
        } else
            System.out.println(result);
        return null;
    }
}