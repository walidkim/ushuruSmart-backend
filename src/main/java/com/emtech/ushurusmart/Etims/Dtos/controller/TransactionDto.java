package com.emtech.ushurusmart.Etims.Dtos.controller;

import java.util.List;


import lombok.Data;

@Data
public class TransactionDto {

   private String ownerPin;

   private String buyerPin;

   private String bussinessPin;
   private List<SaleDto> sales;

}
