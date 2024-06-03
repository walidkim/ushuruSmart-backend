package com.emtech.ushurusmart.payments.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class PaymentDTO {
    private Long id;
    private Long ownerId;
    private String businessKrapin;
    private Long eSlipNumber;
    private String checkoutRequestID;
    private int amount;
    private String payerEmail;
    private String phoneNumber;
    private String mpesaReceipt;
    private LocalDateTime dateInitiated;
    private Boolean isPaid;
    private String paidBy;
    private LocalDateTime transactionDate;
    private LocalDate dateTransaction;

}
