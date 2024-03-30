package com.emtech.ushurusmart.Etims.entity;

import com.emtech.ushurusmart.transactions.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double tax;

    @Column(nullable = false)
    private String ownerPin;
    @Column(nullable = false)
    private String buyerPin;

    @Column(nullable = false)
    private boolean taxable;
    private String invoiceNumber;

    private String etimsNumber;
    @CreationTimestamp
    private LocalDateTime dateCreated;


}
