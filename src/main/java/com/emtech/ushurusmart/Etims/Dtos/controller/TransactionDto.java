package com.emtech.ushurusmart.Etims.Dtos.controller;

import lombok.Data;

@Data
public class TransactionDto {
   private boolean taxable;
    private Double amount;

    private String ownerPin;

    private String buyerPin;

 private String bussinessPin;


}
