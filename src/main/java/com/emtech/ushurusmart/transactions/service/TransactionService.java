package com.emtech.ushurusmart.transactions.service;

import org.springframework.stereotype.Service;
import com.emtech.ushurusmart.Etims.entity.Transaction;
import com.emtech.ushurusmart.Etims.repository.TransactionRepository;
import java.util.List;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Transaction getById(Long id) {
        return transactionRepository.getById(id);
    }

    public Transaction getByBuyerPin(String buyerPin){return transactionRepository.getByBuyerPin(buyerPin);}

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
