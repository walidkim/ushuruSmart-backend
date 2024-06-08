package com.emtech.ushurusmart.payments.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emtech.ushurusmart.payments.entity.PaymentEntity;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    PaymentEntity findByCheckoutRequestID(String checkoutRequestID);


    // List<PaymentEntity> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);

   // List<com.emtech.ushurusmart.etims.entity.Transaction> findByDateTransactionBetween(LocalDate startDate, LocalDate endDate);

    //fetch all transaction
    List<PaymentEntity> findAll();
}

