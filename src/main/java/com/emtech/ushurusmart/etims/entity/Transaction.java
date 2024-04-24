package com.emtech.ushurusmart.etims.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private Double amount;



    @Column(nullable = false)
    private String buyerPin;
    private String invoiceNumber;

    private double tax;
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Sale> sales;

    @ManyToOne
    private Etims etims;
}
