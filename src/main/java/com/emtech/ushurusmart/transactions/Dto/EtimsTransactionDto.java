package com.emtech.ushurusmart.transactions.Dto;

import lombok.Data;

import java.util.List;

@Data
public class EtimsTransactionDto {

   private String ownerPin;

   private String buyerPin;

   private String bussinessPin;
   private List<EtimsProduct> sales;

}
