package com.emtech.ushurusmart.etims_middleware.Dtos;

import lombok.Data;

@Data
public class SaleMiddlewareDto {
    private boolean taxable;
    private Double amount;
    private String name;
}
