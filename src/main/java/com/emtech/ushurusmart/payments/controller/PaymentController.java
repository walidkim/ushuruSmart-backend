package com.emtech.ushurusmart.payments.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/makePayment")
    public ResponseEntity<String> sendSTK(@RequestBody sendPushRequest request) {
        try {
            return paymentImpl.sendSTKPush(request.getPhoneNo(), request.getAmount(), request.getEslipNo());
        } catch (Exception e) {
            log.error("Error occured: " + e);
            return ResponseEntity.badRequest().body("Error occurred! Try later.");
        }
    }

    @PostMapping({ "/callback" })
    public void callback(@RequestBody StkCallbackRequest stkCallbackRequest) throws Exception {
        paymentImpl.callback(stkCallbackRequest);
    }

    @GetMapping({ "/payment-report" })
    public ResponseEntity<List<PaymentEntity>> getPaymentsBetweenDates(
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDateTime endDate) {
        List<PaymentEntity> payment = this.paymentHistService.getPaymentsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(payment);
    }
}