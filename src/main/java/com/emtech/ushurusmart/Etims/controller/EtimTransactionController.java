package com.emtech.ushurusmart.Etims.controller;

import com.emtech.ushurusmart.Etims.Dtos.controller.TransactionDto;
import com.emtech.ushurusmart.Etims.entity.EtimsTransaction;
import com.emtech.ushurusmart.Etims.factory.EntityFactory;
import com.emtech.ushurusmart.Etims.service.EtimsOwnerService;
import com.emtech.ushurusmart.Etims.service.EtimsTransactionService;
import com.emtech.ushurusmart.Etims.service.TaxCalculator;
import com.emtech.ushurusmart.utils.controller.ResContructor;
import com.emtech.ushurusmart.utils.controller.Responses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
