package com.emtech.ushurusmart.etims_middleware.Dtos;

import lombok.Data;

import java.util.List;

@Data
public class TransactionMiddlewareDto {
    private String ownerPin;

    private String buyerPin;

    private String bussinessPin;
    private List<SaleMiddlewareDto> sales;
}
