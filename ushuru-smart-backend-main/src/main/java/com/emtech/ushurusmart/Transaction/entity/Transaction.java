package com.emtech.ushurusmart.Transaction.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
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
}
