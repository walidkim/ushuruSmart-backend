package com.emtech.ushurusmart.Etims.Dtos.controller;

import lombok.Data;

@Data
public class SaleDto {
  private boolean taxable;
  private Double amount;
  private String name;
}
