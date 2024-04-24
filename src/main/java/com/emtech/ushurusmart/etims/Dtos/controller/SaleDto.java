package com.emtech.ushurusmart.etims.Dtos.controller;

import lombok.Data;

@Data
public class SaleDto {
  private boolean taxable;
  private Double amount;
  private String name;
}
