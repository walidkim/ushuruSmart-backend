package com.emtech.ushurusmart.transactions.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="transaction", schema="ushuru_smart")
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable=false)
    private Double amount;
    @Column(nullable=false)
    private String buyerPin;
    private Boolean taxExemption;
    private String invoiceNumber;
    @CreationTimestamp
    private LocalDateTime dateCreated;
    //assume sale transaction has only one product
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    private Integer productQuantity;
    private Double productUnitPrice;
    private String productName;
    private String productId;
}
