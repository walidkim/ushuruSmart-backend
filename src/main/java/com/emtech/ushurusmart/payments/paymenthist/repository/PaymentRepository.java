package com.emtech.ushurusmart.payments.paymenthist.repository;

import com.emtech.ushurusmart.payments.paymenthist.entity.payment;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<payment, Long> {
List<payment> findByTransactionDateBetween (LocalDateTime startDate, LocalDateTime endDate);
}
