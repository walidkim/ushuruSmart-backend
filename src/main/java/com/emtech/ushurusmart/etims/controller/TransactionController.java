package com.emtech.ushurusmart.etims.controller;

import com.emtech.ushurusmart.etims.Dtos.controller.TransactionDto;
import com.emtech.ushurusmart.etims.entity.Etims;
import com.emtech.ushurusmart.etims.entity.Transaction;
import com.emtech.ushurusmart.etims.service.EtimsOwnerService;
import com.emtech.ushurusmart.etims.service.TransactionService;
import com.emtech.ushurusmart.transactions.Dto.EtimsResponses;
import com.emtech.ushurusmart.utils.controller.ResContructor;
import com.emtech.ushurusmart.utils.controller.Responses;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
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


    @Autowired
    private Responses responses;

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
            res.setData(saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (Exception e) {
            return responses.create500Response(e);
        }
    }

    @GetMapping("/generate-excel-monthly-transactions")
    public ResponseEntity<byte[]> downloadExcelReport(HttpServletResponse response) throws IOException {
        byte[] excelData = transactionService.generateExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=transaction_report.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }
    @GetMapping("/transactions/count")
    public Long getTransactionCount() {
        return transactionService.getTransactionCount();
    }

    @GetMapping("get-total-amount-transacted")
    public ResponseEntity<Double> getTransactionAmountTotal() {
        Double total = transactionService.getTransactionHistory();
        return ResponseEntity.ok(total);
    }
    @GetMapping("get-total-tax-transacted")
    public ResponseEntity<Double> getTransactionTaxTotal() {
        Double total = transactionService.getTaxHistory();
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

    @GetMapping("/get-monthly-transaction-by-date")
    public List<Transaction> getTransactionsMonthly(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return transactionService.getTransactionsMonthly(startDate, endDate);
    }

    @GetMapping("/get-current-day-transaction-sum")
    public ResponseEntity<Double> getCurrentDayTransactionSum() {
        Double transactionSum = transactionService.getCurrentDayTransactionSum();
        return ResponseEntity.ok(transactionSum);
    }

}
