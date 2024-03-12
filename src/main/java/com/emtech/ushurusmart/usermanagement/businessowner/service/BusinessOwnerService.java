package com.emtech.ushurusmart.usermanagement.businessowner.service;

import com.emtech.ushurusmart.usermanagement.businessowner.model.BusinessOwner;
import com.emtech.ushurusmart.usermanagement.businessowner.repository.BusinessOwnerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessOwnerService {

    private final BusinessOwnerRepository businessOwnerRepository;

    public BusinessOwnerService(BusinessOwnerRepository repository, BusinessOwnerRepository businessOwnerRepository, BusinessOwner businessOwner) {
        this.businessOwnerRepository = businessOwnerRepository;
    }

    public BusinessOwner save(BusinessOwner businessOwner){
        return businessOwnerRepository.save(businessOwner);
    }

    public void deleteById(Integer id) {
        businessOwnerRepository.deleteById(id);
    }
    public ResponseEntity deleteByBusinessKRAPin(String BusinessKRAPin) {
        businessOwnerRepository.deleteByBusinessKRAPin(BusinessKRAPin);
        return null;
    }

    public List<BusinessOwner> findAll() {
        return businessOwnerRepository.findAll();
    }

    public Optional<BusinessOwner> findByBusinessOwnerKRAPin(String BusinessOwnerKRAPin) {
        return businessOwnerRepository.findByBusinessOwnerKRAPin(BusinessOwnerKRAPin);
    }
    public Optional<BusinessOwner> findByBusinessKRAPin(String BusinessKRAPin) {
        return businessOwnerRepository.findByBusinessKRAPin(BusinessKRAPin);
    }
}
