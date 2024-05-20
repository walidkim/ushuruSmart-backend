package com.emtech.ushurusmart.transactions.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
        private String buyerKRAPin;
        private Double salesAmount;
        private List<TransactionProduct> sales;
}


