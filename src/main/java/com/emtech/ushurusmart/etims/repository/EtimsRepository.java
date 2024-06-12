package com.emtech.ushurusmart.etims.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emtech.ushurusmart.etims.entity.Etims;

import jakarta.transaction.Transactional;

@Repository
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

    @Transactional
    void deleteByBusinessOwnerKRAPin(String businessOwnerKRAPin);
}
