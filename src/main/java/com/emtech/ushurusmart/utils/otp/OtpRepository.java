package com.emtech.ushurusmart.utils.otp;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    List<OtpEntity> findByUserTag(String usertag);

    Optional<OtpEntity> findByUserTagAndOtpCode(String usertag, String otpcode);

    @Transactional
    @Query("DELETE FROM OtpEntity e WHERE e.validUntil <= CURRENT_TIMESTAMP")
    void deleteByValidUntilOrBeforeNow();

    @Transactional
    void deleteByUserTag(String usertag);
}