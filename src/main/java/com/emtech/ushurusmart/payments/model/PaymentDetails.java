package com.emtech.ushurusmart.payments.model;

import com.emtech.ushurusmart.usermanagement.model.Owner;
import jakarta.persistence.*;
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
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    private String method;

    @Column(name = "payment_code")
    private String paymentCode;

    @Column(name = "e_slip_number", nullable = false)
    private Long eSlipNumber;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid;
}