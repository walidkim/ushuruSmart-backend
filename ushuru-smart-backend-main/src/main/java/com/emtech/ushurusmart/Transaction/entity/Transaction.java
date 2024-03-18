package com.emtech.ushurusmart.Transaction.entity;

import jakarta.persistence.*;
<<<<<<< HEAD
import lombok.*;
=======
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.NoArgsConstructor;
>>>>>>> b12f94f (changes)
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
<<<<<<< HEAD
@Getter
@Setter
=======
>>>>>>> b12f94f (changes)
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
<<<<<<< HEAD
    private String invoiceNumber;
    @CreationTimestamp
    private LocalDateTime dateCreated;
//assume sale transaction has only one product
    @ManyToOne(fetch = FetchType.LAZY)
    private Products product;
    private Integer productQuantity;
    private Double productUnitPrice;
    private String productName;
    private String productId;
=======
    @CreationTimestamp
    private LocalDateTime dateCreated;
>>>>>>> b12f94f (changes)
}
