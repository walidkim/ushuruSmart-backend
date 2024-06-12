package com.emtech.ushurusmart.payments.controller;



import com.emtech.ushurusmart.payments.Utils.DateUtils;
import com.emtech.ushurusmart.payments.service.EslipService;
import com.emtech.ushurusmart.payments.service.PaymentService;
import com.emtech.ushurusmart.payments.service.TaxCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.emtech.ushurusmart.payments.entity.PaymentEntity;
import com.emtech.ushurusmart.payments.repository.PaymentRepository;
import com.emtech.ushurusmart.payments.Utils.PaymentMapper;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import com.emtech.ushurusmart.payments.dtos.PaymentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.emtech.ushurusmart.payments.dtos.sendPushRequest;
import com.emtech.ushurusmart.payments.dtos.callback.StkCallbackRequest;
import com.emtech.ushurusmart.payments.service.PaymentImpl;

@RestController
@RequestMapping("/api/v1/payment/")
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private final PaymentImpl paymentImpl;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    PaymentMapper paymentMapper;

    @Autowired
    private final TaxCalculationService taxCalculationService;
    private final EslipService eslipService;

    @Autowired
    public PaymentController(PaymentImpl paymentImpl, TaxCalculationService taxCalculationService, EslipService eslipService) {
        this.paymentImpl = paymentImpl;

        this.taxCalculationService = taxCalculationService;
        this.eslipService = eslipService;
    }

    @PostMapping("/makePayment")
    public ResponseEntity<String> sendSTKPush(@RequestBody sendPushRequest request) {
        try {
            return paymentImpl.sendSTKPush(request.getPhoneNo(), request.getAmount(), request.getEslipNo());
        } catch (Exception e) {
            log.error("Error occured: " + e);
            return ResponseEntity.badRequest().body("Error occurred! Try later.");
        }
    }


    @PostMapping({"/callback"})
    public void callback(@RequestBody StkCallbackRequest stkCallbackRequest) throws Exception {
        paymentImpl.callback(stkCallbackRequest);
    }

    @GetMapping("/generateESlip")
    public ResponseEntity<String> generateESlip() {
        String eslip = eslipService.createESlipFromDB();
        return ResponseEntity.ok(eslip);
    }

    @GetMapping("/taxDue")
    public ResponseEntity<String> getTaxDue(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr) {

        LocalDate startDate = DateUtils.parseDate(startDateStr);
        LocalDate endDate = DateUtils.parseDate(endDateStr);

        double totalTax = taxCalculationService.calculateTotalTax(startDate, endDate);
        String responseMessage = "Total tax due from " + startDate + " to " + endDate + " is: " + totalTax;

        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/payment-report")
    public ResponseEntity<List<PaymentDTO>> getPaymentsBetweenDates(
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay().plusDays(1).minusNanos(1);
        log.info(endDateTime.toString());
        List<PaymentEntity> payments = paymentRepository.findByTransactionDateBetween(startDateTime, endDateTime);
        //List<PaymentEntity> payments= paymentRepository.findAll();

        List<PaymentDTO> paymentDTOs = payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());

        log.info(paymentDTOs.toString());
        return ResponseEntity.ok(paymentDTOs);
    }
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException ex) {
        String message = "Incorrect date format.Enter (dd/MM/yyyy)";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }



}