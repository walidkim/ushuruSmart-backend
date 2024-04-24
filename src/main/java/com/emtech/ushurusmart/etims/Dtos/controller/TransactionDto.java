package com.emtech.ushurusmart.etims.Dtos.controller;

import lombok.Data;

import java.util.List;

@Data
public class TransactionDto {

   private String ownerPin;

   private String buyerPin;

   private String bussinessPin;
   private List<SaleDto> sales;

}
