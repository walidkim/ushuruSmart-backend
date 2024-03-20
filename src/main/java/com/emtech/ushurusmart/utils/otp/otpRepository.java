package com.emtech.ushurusmart.utils.otp;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface otpRepository extends JpaRepository<otpEntity, Long> {
    List<otpEntity> findByUsertag(String usertag);

    List<otpEntity> findByUsertagAndOtpcode(String usertag, String otpcode);

    void deleteByCreatedAtBefore(LocalDateTime expiryThreshold);

    void deleteByUsertag(String usertag);
}