package com.emtech.ushurusmart.payments.entity;

import java.time.LocalDateTime;

import com.emtech.ushurusmart.usermanagement.model.Owner;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "payment")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Owner owner;
    @Column(name = "business_kra", nullable = false)
    private String business_krapin;
    @Column(name = "e_slip_number", nullable = false)
    private Long eSlipNumber;
    @Column(name = "checkoutRequestID")
    private String checkoutRequestID;
    @Column(name = "amount", nullable = false)
    private int amount;
    @Column(name = "payerEmail", nullable = false)
    private String payerEmail;
    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;
    @Column(name = "mpesa_receipt")
    private String mpesa_receipt;
    @Column(name = "date_Initiated", nullable = false)
    private LocalDateTime date_initiated;
    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid;
    @Column(name = "paid_by")
    private String paidBy;
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

}