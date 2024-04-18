package com.emtech.ushurusmart.Etims.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emtech.ushurusmart.Etims.entity.EtimsTransaction;

@Repository
public interface TransactionRepository extends JpaRepository<EtimsTransaction, Long> {
    EtimsTransaction getByBuyerPin(String buyerPin);

    EtimsTransaction findByInvoiceNumber(String invoiceNumber);

    List<EtimsTransaction> findByDateCreatedBetween(LocalDate startDate, LocalDate endDate);

    List<EtimsTransaction> findByOwnerPin(String ownerPin);

    @Query("SELECT t FROM EtimsTransaction t WHERE t.dateCreated = :date")
    List<EtimsTransaction> findByTransactionDate(@Param("date") LocalDate date);

    @Query("SELECT t FROM Transaction t WHERE t.dateCreated BETWEEN :startDate AND :endDate")
    List<EtimsTransaction> findByTransactionDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
