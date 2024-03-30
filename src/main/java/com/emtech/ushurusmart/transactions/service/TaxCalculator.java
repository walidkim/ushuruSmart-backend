package com.emtech.ushurusmart.transactions.service;

import org.springframework.stereotype.Service;

@Service
public class TaxCalculator implements TaxCalculatortInt {
    public double calculateTax(boolean taxiability, double price) {
        return getTaxPercentage(taxiability) * price;
    }

    private double getTaxPercentage(boolean isTaxable) {
        return isTaxable? 0.16 : 0.0;
    }
}
