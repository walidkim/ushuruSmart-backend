package com.emtech.ushurusmart.etims.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
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

    private LocalDate date;

    @Column(nullable = false)
    private String buyerPin;
    private String invoiceNumber;

    private double tax;
    public Transaction(double tax) {
        this.tax = tax;
    }
    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }
    @CreationTimestamp
    private LocalDate dateCreated;
    private LocalDateTime transactionDate;

    @OneToMany(fetch = FetchType.LAZY)
    private List<com.emtech.ushurusmart.etims.entity.Sale> sales;

    @ManyToOne
    private com.emtech.ushurusmart.etims.entity.Etims etims;
}
