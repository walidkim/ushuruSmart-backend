package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.usermanagement.Dtos.ResContructor;
import com.emtech.ushurusmart.usermanagement.model.Etims;
import com.emtech.ushurusmart.usermanagement.repository.EtimsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private EtimsRepository etimsRepository;

    public ResContructor etimsSave(Etims data) {
        etimsRepository.save(data);
        ResContructor res = new ResContructor();
        res.setMessage("Business Owner added successfully!");
        return res;
    }

    public ResContructor etimsUpdate(Etims data) {
        if (!etimsRepository.findByBusinessOwnerKRAPin(data.getBusinessOwnerKRAPin())) {
            ResContructor res = new ResContructor();
            res.setMessage("Business Owner not found!");
            return res;
        }
        etimsRepository.save(data);
        ResContructor res = new ResContructor();
        res.setMessage("Business Owner updated successfully!");
        return res;
    }

    public ResContructor etimsDelete(String businessOwnerKRAPin) {
        if (!etimsRepository.findByBusinessKRAPin(businessOwnerKRAPin)) {
            ResContructor res = new ResContructor();
            res.setMessage("Business Owner not found!");
            return res;
        }
        etimsRepository.deleteById(Long.valueOf(businessOwnerKRAPin));
        ResContructor res = new ResContructor();
        res.setMessage("Business Owner deleted successfully!");
        return res;
    }
}