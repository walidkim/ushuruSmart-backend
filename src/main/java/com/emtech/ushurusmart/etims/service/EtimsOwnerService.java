package com.emtech.ushurusmart.etims.service;

import com.emtech.ushurusmart.etims.entity.Etims;
import com.emtech.ushurusmart.etims.repository.EtimsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.emtech.ushurusmart.utils.service.GeneratorService.generateRandomString;

@Service
public class EtimsOwnerService {

    @Autowired
    private EtimsRepository etimsRepository;

    public Etims findByBusinessKRAPin(String businessKRAPin) {
        Optional<Etims> etims = etimsRepository.findByBusinessKRAPin(businessKRAPin);
        return etims.orElse(null);
    }



    public Etims save(Etims data) {
        data.setEtimsCode(generateRandomString(32));
        Etims res = etimsRepository.save(data);
       return res;
    }

    public Etims etimsUpdate(Etims data) {
        if (!etimsRepository.findByBusinessOwnerKRAPin(data.getBusinessOwnerKRAPin()).isPresent()) {
            return null;
        }
        return etimsRepository.save(data);
    }

    public boolean etimsDelete(String businessOwnerKRAPin) {
        try {
            etimsRepository.deleteByBusinessOwnerKRAPin(businessOwnerKRAPin);
            return true;

        } catch (Exception e) {
            return false;
        }

    }

    public Etims findByBusinessOwnerKRAPin(String businessOwnerKRAPin) {
        Optional<Etims> result = etimsRepository.findByBusinessOwnerKRAPin(businessOwnerKRAPin);
        return result.orElse(null);

    }

    public Etims findByBusinnessKRAPin(String businessKRAPin) {
        Optional<Etims> result = etimsRepository.findByBusinessKRAPin(businessKRAPin);
        return result.orElse(null);

    }

    public long count() {
        return etimsRepository.count();
    }
}