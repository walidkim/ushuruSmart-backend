package com.emtech.ushurusmart.transactions.Dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
        private String buyerKRAPin;
        private List<TransactionProduct> products;
}


