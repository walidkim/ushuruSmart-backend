package com.emtech.ushurusmart.transactions.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private String buyerKRAPin;
    private long productId;
    private int quantity;
}
