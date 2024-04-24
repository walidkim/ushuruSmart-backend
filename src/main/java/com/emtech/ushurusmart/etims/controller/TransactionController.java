package com.emtech.ushurusmart.etims.controller;

import com.emtech.ushurusmart.etims.Dtos.controller.TransactionDto;
import com.emtech.ushurusmart.etims.entity.Etims;
import com.emtech.ushurusmart.etims.entity.Transaction;
import com.emtech.ushurusmart.etims.service.EtimsOwnerService;
import com.emtech.ushurusmart.etims.service.TransactionService;
import com.emtech.ushurusmart.utils.controller.ResContructor;
import com.emtech.ushurusmart.utils.controller.Responses;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/etims/tax")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private EtimsOwnerService etimsOwnerService;

    @PostMapping("/make-transaction")
    public ResponseEntity<ResContructor> makeTransaction(@RequestBody TransactionDto data) {
        try {
            ResContructor res = new ResContructor();
            Etims owner = etimsOwnerService.findByBusinessKRAPin(data.getBussinessPin());
            res.setMessage("Transaction recorded successfully.");
            if (owner == null) {
                res.setMessage("Business is not registered by Etims.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
            }
            Transaction transaction = transactionService.createTransaction(data, owner);

            Transaction saved = transactionService.save(transaction);
            saved.setEtims(null);

            res.setData(saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (Exception e) {
            return Responses.create500Response(e);
        }
    }

    @GetMapping("/generate-TransactionXLS-report")
    public void generateExcelReport(HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = transactionReport.xls";
        response.setHeader(headerKey, headerValue);
        transactionService.generateExcel(response);
    }

    @GetMapping("transaction-amount-history")
    public ResponseEntity<Double> getTransactionAmountTotal() {
        Double total = transactionService.getTransactionHistory();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/daily")
    public List<Transaction> getTransactionToday() {
        return transactionService.getTransactionToday();
    }

    @GetMapping("/daily/{date}")
    public List<Transaction> getTransactionsDaily(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return transactionService.getTransactionsDaily(date);
    }

    @GetMapping("/monthly")
    public List<Transaction> getTransactionsMonthly(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return transactionService.getTransactionsMonthly(startDate, endDate);
    }

}
