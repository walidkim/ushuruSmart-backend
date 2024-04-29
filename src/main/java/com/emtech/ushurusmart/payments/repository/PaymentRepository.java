package com.emtech.ushurusmart.payments.repository;

import com.emtech.ushurusmart.payments.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
List<PaymentEntity> findByTransactionDateBetween (LocalDateTime startDate, LocalDateTime endDate);
}
