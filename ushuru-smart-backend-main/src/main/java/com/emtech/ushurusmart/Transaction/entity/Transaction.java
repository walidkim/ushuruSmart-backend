package com.emtech.ushurusmart.Transaction.entity;

import jakarta.persistence.*;
<<<<<<< HEAD
<<<<<<< HEAD
import lombok.*;
=======
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.NoArgsConstructor;
>>>>>>> b12f94f (changes)
=======
import lombok.*;
>>>>>>> 4476ce9 (Credit Note)
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
<<<<<<< HEAD
<<<<<<< HEAD
@Getter
@Setter
=======
>>>>>>> b12f94f (changes)
=======
@Getter
@Setter
>>>>>>> 4476ce9 (Credit Note)
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
<<<<<<< HEAD
=======
>>>>>>> 4476ce9 (Credit Note)
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
<<<<<<< HEAD
=======
    @CreationTimestamp
    private LocalDateTime dateCreated;
>>>>>>> b12f94f (changes)
=======
>>>>>>> 4476ce9 (Credit Note)
}
