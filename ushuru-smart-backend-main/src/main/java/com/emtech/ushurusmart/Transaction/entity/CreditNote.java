package com.emtech.ushurusmart.Transaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditNote {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long id;
        private String invoiceNumber;
        private String buyerPin;
        private String productId;
        private Double productAmount;
        private String productName;
        private Integer productQuantity;
}
