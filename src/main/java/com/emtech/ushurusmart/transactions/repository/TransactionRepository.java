package com.emtech.ushurusmart.transactions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.emtech.ushurusmart.transactions.entity.Transaction;

public interface TransactionRepository extends JpaRepository <Transaction, Long>{
    Transaction getByBuyerPin(String buyerPin);

    Transaction findByInvoiceNumber(String invoiceNumber);
}
