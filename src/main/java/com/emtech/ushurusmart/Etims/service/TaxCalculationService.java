package com.emtech.ushurusmart.etims.service;

import com.emtech.ushurusmart.etims.entity.Transaction;
import com.emtech.ushurusmart.etims.repository.TransactionRepository;
import com.emtech.ushurusmart.transactions.entity.Product;
import com.emtech.ushurusmart.transactions.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaxCalculationService {
    @Autowired
    private TransactionRepository transactionRepository;

    public double calculateTotalTax(LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findByDateBetween(startDate, endDate);
        double totalAmount = transactions.stream().mapToDouble(Transaction::getAmount).sum();
        double totalTax = totalAmount * 0.16; // Assuming a 16% tax rate
        return totalTax;
    }
}

