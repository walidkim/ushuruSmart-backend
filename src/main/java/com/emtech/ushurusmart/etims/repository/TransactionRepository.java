package com.emtech.ushurusmart.etims.repository;


import com.emtech.ushurusmart.etims.entity.Transaction;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction getByBuyerPin(String buyerPin);

    Transaction findByInvoiceNumber(String invoiceNumber);

    List<Transaction> findByDateCreatedBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t JOIN t.etims e WHERE e.businessOwnerKRAPin = :ownerPin")
    List<Transaction> findByOwnerPin(@Param("ownerPin") String ownerPin);

    @Query("SELECT t FROM Transaction t WHERE t.dateCreated = :date")
    List<Transaction> findByTransactionDate(@Param("date") LocalDate date);

    @Query("SELECT t FROM Transaction t WHERE t.dateCreated BETWEEN CAST(:startDate AS TIMESTAMP) AND CAST(:endDate AS TIMESTAMP)")
    List<Transaction> findByTransactionDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<Transaction> findByTransactionDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);


//    List<Transaction> findByTransactionDateBetween(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);


//    @Query("SELECT t FROM Transaction t WHERE t.dateCreated BETWEEN :startDate AND :endDate")
//    List<EtimsTransaction> findByTransactionDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
