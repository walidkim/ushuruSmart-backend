package com.emtech.ushurusmart.Transaction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="transactions", schema="ushuru_smart")
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long id;
    @Column(nullable=false)
    private double amount;
    @Column(nullable=false)
    private String buyerPin;
    private boolean taxExemption;
    @CreationTimestamp
    private LocalDateTime dateCreated;
}
