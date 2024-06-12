package com.emtech.ushurusmart.usermanagement.Dtos.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDto {
    private String description;
    private String name;
    private int quantity;
    private boolean taxable;
    private String type;
    private String unitOfMeasure;
    private double unitPrice;
}
