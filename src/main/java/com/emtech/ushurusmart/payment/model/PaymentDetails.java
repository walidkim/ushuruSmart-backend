package com.emtech.ushurusmart.payment.model;

import com.emtech.ushurusmart.usermanagement.model.Admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "payments")
public class PaymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double amount;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    private String method;

    @Column(name = "payment_code")
    private String paymentCode;

    @Column(name = "e_slip_number", nullable = false)
    private Long eSlipNumber;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid;
}