package com.emtech.ushurusmart.Transaction.repository;
import com.emtech.ushurusmart.Transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;



public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction getByBuyerPin(String buyerPin);
<<<<<<< HEAD

    Transaction findByInvoiceNumber(String invoiceNumber);
=======
>>>>>>> b12f94f (changes)
}

