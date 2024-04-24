package com.emtech.ushurusmart.payments.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "e_slip_number", nullable = false)
    private Long eSlipNumber;

    @Column(name = "checkoutRequestID")
    private String checkoutRequestID;

    @Column(name="amount", nullable = false)
            private double amount;
    @Column(name="paid_by",  nullable = false)
    private String phoneNumber;

    @Column(name = "mpesa_receipt")
    private String mpesa_receipt;

    @Column(name="date_Initiated", nullable=false)
    private LocalDateTime date_initiated;


    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid;

    @Column(name="transaction_date")
    private LocalDateTime transactionDate;


}