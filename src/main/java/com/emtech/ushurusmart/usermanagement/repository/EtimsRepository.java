package com.emtech.ushurusmart.usermanagement.repository;

import com.emtech.ushurusmart.usermanagement.model.Etims;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EtimsRepository extends JpaRepository<Etims, Long> {
    // @Query("SELECT e.businessKRAPins FROM Etims e")
    // List<String> findAllBusinessKraPins();
    //
    // // Get all KRA PINS for business owners
    // @Query("SELECT e.businessOwnerKRAPIns FROM Etims e")
    // List<String> findAllBusinessOwnerKraPins();

    // Find etims by business kra pin
    Optional<Etims> findByBusinessKRAPin(String businessKRAPin);

    // Find etims by business owner kra pin
    Optional<Etims> findByBusinessOwnerKRAPin(String businessOwnerKRAPin);
}
