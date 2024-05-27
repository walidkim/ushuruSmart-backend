package com.emtech.ushurusmart.payments.service;

import com.emtech.ushurusmart.etims.entity.Transaction;
import com.emtech.ushurusmart.etims.repository.TransactionRepository;
import com.emtech.ushurusmart.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public abstract class TaxCalculationService implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public List<Transaction> getTransactionsMonthly(LocalDate startDate, LocalDate endDate) {
        LocalDate startDateTime = LocalDate.from(startDate.atStartOfDay());
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();
        return transactionRepository.findByTransactionDateBetween(startDateTime, LocalDate.from(endDateTime));
    }

    public double calculateTotalTax(LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = getTransactionsMonthly(startDate, endDate);
        return transactions.stream().mapToDouble(Transaction::getTax).sum();
    }
}