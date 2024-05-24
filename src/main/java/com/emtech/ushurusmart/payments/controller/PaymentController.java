package com.emtech.ushurusmart.payments.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.emtech.ushurusmart.payments.dtos.sendPushRequest;
import com.emtech.ushurusmart.payments.dtos.callback.StkCallbackRequest;
import com.emtech.ushurusmart.payments.entity.PaymentEntity;
import com.emtech.ushurusmart.payments.service.PaymentHistService;
import com.emtech.ushurusmart.payments.service.PaymentImpl;

@RestController
@RequestMapping("/api/v1/payment/")
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentImpl paymentImpl;
    @Autowired
    private PaymentHistService paymentHistService;

    public PaymentController(PaymentImpl paymentImpl) {
        this.paymentImpl = paymentImpl;

    }

    @PostMapping("/sendPush")
    public ResponseEntity<String> sendSTK(@RequestBody sendPushRequest request) {
        try {
            return paymentImpl.sendSTK(request.getPhoneNo(), request.getAmount(), request.getEslipNo());
        } catch (Exception e) {
            log.error("Error occured: " + e);
            return ResponseEntity.badRequest().body("Error occurred! Try later.");
        }
    }

    @PostMapping({"/callback"})
    public void callback(@RequestBody StkCallbackRequest stkCallbackRequest) throws Exception {
        paymentImpl.callback(stkCallbackRequest);
    }

    @GetMapping({"/payment-report"})
    public ResponseEntity<List<PaymentEntity>> getPaymentsBetweenDates(
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay().plusDays(1).minusNanos(1);
        List<PaymentEntity> payment = this.paymentHistService.getPaymentsBetweenDates(startDateTime, endDateTime);
        return ResponseEntity.ok(payment);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException ex) {
        String message = "Incorrect date format.Enter (dd/MM/yyyy)";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}