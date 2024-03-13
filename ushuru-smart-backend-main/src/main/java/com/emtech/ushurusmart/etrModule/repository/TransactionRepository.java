package com.emtech.ushurusmart.etrModule.repository;

import com.emtech.ushurusmart.etrModule.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;



public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

