package com.emtech.ushurusmart.transactions.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emtech.ushurusmart.transactions.Dto.TransactionDateRequest;
import com.emtech.ushurusmart.transactions.service.ReportService;

@RestController
@RequestMapping("/api/tax/")
public class ReportController {
    @Autowired
    ReportService reportService;

    @PostMapping("/getReport")
    public ResponseEntity<byte[]> getReport(@RequestBody TransactionDateRequest req) throws IOException {

        try {
            // String Date = "2024-03-01";
            // String Date2 = "2024-03-30";
            return reportService.writeTransactionsToExcel(req.getStartDate(), req.getEndDate());
        } catch (Exception e) {
            throw new RuntimeException("Error getting the report" + e);
        }
    }
}