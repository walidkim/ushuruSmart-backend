package com.emtech.ushurusmart.transactions.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditNote {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String invoiceNumber;
    private String buyerPin;
    private String productId;
    private Double productAmount;
    private String productName;
    private Integer productQuantity;
}
