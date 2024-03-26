package com.emtech.ushurusmart.usermanagement.repository;

import com.emtech.ushurusmart.usermanagement.model.Etims;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EtimsRepository extends JpaRepository<Etims, Long> {


    Optional<Etims> findByBusinessKraPin(String businesskrapin);

    Optional<Etims> findByBusinessOwnerKraPin(String businessOwnerKrapin);

}
