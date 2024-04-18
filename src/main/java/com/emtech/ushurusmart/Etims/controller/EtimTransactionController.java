package com.emtech.ushurusmart.Etims.controller;

import com.emtech.ushurusmart.Etims.Dtos.controller.TransactionDto;
import com.emtech.ushurusmart.Etims.entity.EtimsTransaction;
import com.emtech.ushurusmart.Etims.factory.EntityFactory;
import com.emtech.ushurusmart.Etims.service.EtimsOwnerService;
import com.emtech.ushurusmart.Etims.service.EtimsTransactionService;
import com.emtech.ushurusmart.Etims.service.TaxCalculator;
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
@RequestMapping("/etims/tax")
public class EtimTransactionController {

    @Autowired
    private EtimsTransactionService transactionService;

    @Autowired
    private EtimsOwnerService etimsOwnerService;

    @Autowired
    private  TaxCalculator taxCalculatorService;

    @PostMapping("/make-transaction")
    public ResponseEntity<ResContructor> makeTransaction(@RequestBody TransactionDto data) {
        try {
            ResContructor res = new ResContructor();
            res.setMessage("Registered the owner successfully.");
            if(etimsOwnerService.findByBusinessKRAPin(data.getBussinessPin())==null){
               res.setMessage("Business is not registered by Etims.");
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
            }
            EtimsTransaction transaction= EntityFactory.createTransaction(data);

            double tax = taxCalculatorService.calculateTax(data.isTaxable(), data.getAmount());
            transaction.setTax(tax);

            EtimsTransaction saved= transactionService.save(transaction);

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
        response.setHeader(headerKey,headerValue);
        transactionService.generateExcel(response);
    }

    @GetMapping("transaction-amount-history")
    public ResponseEntity<Double> getTransactionAmountTotal(){
        Double total = transactionService.getTransactionHistory();
        return ResponseEntity.ok(total);
    }
    @GetMapping("/daily/{date}")
    public List<EtimsTransaction> getTransactionsDaily(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
       return transactionService.getTransactionsDaily(date);
    }
    @GetMapping("/monthly")
    public List<EtimsTransaction> getTransactionsMonthly(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return transactionService.getTransactionsMonthly(startDate,endDate);
    }


}
