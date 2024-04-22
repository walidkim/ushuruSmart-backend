package com.emtech.ushurusmart.payments.paymenthist.controller;

import com.emtech.ushurusmart.payments.paymenthist.entity.payment;
import com.emtech.ushurusmart.payments.paymenthist.service.PaymentHistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/Payment_History")
public class PaymentHistController {
    @Autowired
    private PaymentHistService paymentHistService;
@GetMapping("/between-dates")
    public ResponseEntity<List<payment>> getPaymentsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate) {
        List<payment> payments = paymentHistService.getPaymentsBetweenDates(startDate,endDate);
        return ResponseEntity.ok(payments);
    }
}






