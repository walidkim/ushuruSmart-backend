package com.emtech.ushurusmart.usermanagement.Dtos.entity;


import lombok.Data;

@Data
public class ProductDto {
    private String description;
    private String name;
    private int quantity;
    private boolean taxExempted;
    private String type;
    private String unitOfMeasure;
    private double unitPrice;
}
