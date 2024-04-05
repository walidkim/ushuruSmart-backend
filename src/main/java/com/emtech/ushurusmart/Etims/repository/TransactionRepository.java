package com.emtech.ushurusmart.Etims.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emtech.ushurusmart.Etims.entity.EtimsTransaction;

@Repository
public interface TransactionRepository extends JpaRepository<EtimsTransaction, Long> {
    EtimsTransaction getByBuyerPin(String buyerPin);

    EtimsTransaction findByInvoiceNumber(String invoiceNumber);

    List<EtimsTransaction> findByDateCreatedBetween(LocalDateTime startDate, LocalDateTime endDate);
}
