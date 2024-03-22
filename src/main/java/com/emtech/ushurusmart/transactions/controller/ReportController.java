package com.emtech.ushurusmart.transactions.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emtech.ushurusmart.transactions.Dto.TransactionReportRequest;
import com.emtech.ushurusmart.transactions.repository.TransactionRepository;

@RestController
@RequestMapping("/api/tax/report")
public class ReportController {

    TransactionRepository transactionRepository;
    TransactionReportRequest request;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // e.g.,

    @GetMapping("/tranx")
    @PostMapping("/generateTransactionReport")
    public String generateTransactionReport(@RequestBody TransactionReportRequest request) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime startDateTime = LocalDateTime.parse(request.getstartDate() + "T00:00:00", formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(request.getendDate() + "T23:59:59", formatter);
            // Use startDateTime and endDateTime in your logic
            transactionRepository.findByDateCreatedBetween(startDateTime, endDateTime);
            return "Transaction report generated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating transaction report";
        }
    }
}
