package com.emtech.ushurusmart.Etims.service;

import com.emtech.ushurusmart.Etims.entity.Etims;
import com.emtech.ushurusmart.Etims.repository.EtimsRepository;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.emtech.ushurusmart.utils.service.GeneratorService.generateRandomString;

@Service
public class EtimsOwnerService {

    @Autowired
    private EtimsRepository etimsRepository;

    public Etims findByBusinessKRAPin(String businessKRAPin) {
        Optional<Etims> etims = etimsRepository.findByBusinessKRAPin(businessKRAPin);
        if (etims.isPresent()) {
            return etims.get();
        } else
            return null;
    }



    public Etims save(Etims data) {
        data.setEtimsCode(generateRandomString(32));
        Etims res = etimsRepository.save(data);
        System.out.println(res);
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
            System.out.println(e.getLocalizedMessage());
            return false;
        }

    }

    public Etims findByBusinessOwnerKRAPin(String businessOwnerKRAPin) {
        Optional<Etims> result = etimsRepository.findByBusinessOwnerKRAPin(businessOwnerKRAPin);
        return result.orElse(null);

    }

    public long count() {
        return etimsRepository.count();
    }
}