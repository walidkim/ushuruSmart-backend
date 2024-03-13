package com.emtech.ushurusmart.Transaction.service;
import com.emtech.ushurusmart.Transaction.entity.Transaction;
import com.emtech.ushurusmart.Transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Transaction getById(long id) {
        return transactionRepository.getById(id);
    }

    public Transaction getByBuyerPin(String buyerPin){return transactionRepository.getByBuyerPin(buyerPin);}

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

}
