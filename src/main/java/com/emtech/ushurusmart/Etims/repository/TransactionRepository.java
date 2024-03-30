package com.emtech.ushurusmart.Etims.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emtech.ushurusmart.Etims.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction getByBuyerPin(String buyerPin);

    Transaction findByInvoiceNumber(String invoiceNumber);

    List<Transaction> findByDateCreatedBetween(LocalDateTime startDate, LocalDateTime endDate);
}
