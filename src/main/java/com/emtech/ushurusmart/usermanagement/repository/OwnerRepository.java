package com.emtech.ushurusmart.usermanagement.repository;

import com.emtech.ushurusmart.usermanagement.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Owner findByEmail(String email);

    Owner deleteByEmail(String email);


    Owner findByPhoneNumber(String phoneNumber);
    Optional<Owner> findById(Long id);
}
