package com.emtech.ushurusmart.transactions.Dto;

import lombok.Data;

@Data
public class EtimsProduct {
  private boolean taxable;
  private Double amount;
  private String name;
}
