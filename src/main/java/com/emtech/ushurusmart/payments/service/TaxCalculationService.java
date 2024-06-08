package com.emtech.ushurusmart.payments.service;

import com.emtech.ushurusmart.etims.entity.Transaction;
import com.emtech.ushurusmart.etims.repository.TransactionRepository;
import com.emtech.ushurusmart.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Service
public abstract class TaxCalculationService implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public List<Transaction> getTransactionsMonthly(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay(); // Include the end date
        return transactionRepository.findByTransactionDateBetween(startDateTime, endDateTime);
    }

    public double calculateTotalTax(LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = getTransactionsMonthly(startDate, endDate);
        return transactions.stream().mapToDouble(Transaction::getTax).sum();
    }

    public void showTaxDue(LocalDate startDate, LocalDate endDate) {
        double totalTax = calculateTotalTax(startDate, endDate);
        System.out.println("Total tax due from " + startDate + " to " + endDate + " is: " + totalTax);
    }
}