package com.emtech.ushurusmart.transactions.service;

import org.springframework.stereotype.Service;

@Service
public class TaxCalculator implements TaxCalculatortInt {
    public double calculateTax(String productType, double price) {
        return getTaxPercentage(productType) * price;
    }

    private double getTaxPercentage(String productType) {
        double taxPercentage = 0.0;
        switch (productType.toLowerCase()) {
            case "taxable":
                taxPercentage = 0.16;
                break;
            case "free":
                taxPercentage = 0.0;
                break;

            default:
                throw new IllegalArgumentException("Unknown product type: " + productType);
        }

        return taxPercentage;
    }
}
