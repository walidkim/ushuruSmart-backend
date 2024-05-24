package com.emtech.ushurusmart.payments.controller;

import com.emtech.ushurusmart.payments.entity.PaymentEntity;
import com.emtech.ushurusmart.payments.service.PaymentHistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/Payment_History")
public class PaymentHistController {
    @Autowired
    private PaymentHistService paymentHistService;
@GetMapping("/between-dates")
    public ResponseEntity<List<PaymentEntity>> getPaymentsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    LocalDateTime startDateTime = startDate.atStartOfDay();
    LocalDateTime endDateTime = endDate.atStartOfDay().plusDays(1).minusNanos(1);
        List<PaymentEntity> payment = paymentHistService.getPaymentsBetweenDates(startDateTime,endDateTime);
        return ResponseEntity.ok(payment);
    }
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException ex){
    String message= "Incorrect date format.Enter (yy-mm-dd)";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message) ;
    }
}






