package com.emtech.ushurusmart.etrModule.Dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    private double invoiceNumber;
    private double amount;
    private LocalDateTime date;
    private String buyerPin;
}
