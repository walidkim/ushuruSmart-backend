package com.emtech.ushurusmart.transactions.Dto;

import java.util.List;

import lombok.Data;

@Data
public class EtimsTransactionDto {

   private String ownerPin;

   private String buyerPin;

   private String bussinessPin;
   private List<EtimsProduct> products;

}
