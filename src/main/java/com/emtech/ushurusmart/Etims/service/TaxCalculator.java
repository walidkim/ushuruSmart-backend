package com.emtech.ushurusmart.Etims.service;

import com.emtech.ushurusmart.transactions.service.TaxCalculatortInt;
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
