package com.emtech.ushurusmart.payments.controller;



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
import com.emtech.ushurusmart.payments.dtos.taxDue;
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
    private final PaymentImpl paymentImpl;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    PaymentMapper paymentMapper;


    public PaymentController(PaymentImpl paymentImpl) {
        this.paymentImpl = paymentImpl;

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

    @GetMapping("/taxDue")
    public String calculateTax(@ModelAttribute taxDue request) {
        try {
            LocalDate start = request.getStartDate();
            LocalDate end = request.getEndDate();
            return "This";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

//    @GetMapping({"/payment-report"})
//    public ResponseEntity<List<PaymentEntity>> getPaymentsBetweenDates(
//            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
//            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate) {
//        LocalDateTime startDateTime = startDate.atStartOfDay();
//        LocalDateTime endDateTime = endDate.atStartOfDay().plusDays(1).minusNanos(1);
//       // List<PaymentEntity> payment = this.paymentHistService.getPaymentsBetweenDates(startDate,endDate);
//        List<PaymentEntity> payment = paymentRepository.findAll();
//        log.info(payment.toString());
//        return ResponseEntity.ok(payment);
//    }
@GetMapping({"/payment-report"})
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