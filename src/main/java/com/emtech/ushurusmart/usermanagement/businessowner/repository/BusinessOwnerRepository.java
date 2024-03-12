package com.emtech.ushurusmart.usermanagement.businessowner.repository;

import com.emtech.ushurusmart.usermanagement.businessowner.model.BusinessOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessOwnerRepository extends JpaRepository<BusinessOwner, Integer> {
    Optional<BusinessOwner> findByBusinessKRAPin(String BusinessKRAPin);
    Optional<BusinessOwner> findByBusinessOwnerKRAPin(String BusinessOwnerKRAPin);

    void deleteByBusinessKRAPin(String businessKRAPin);
}
