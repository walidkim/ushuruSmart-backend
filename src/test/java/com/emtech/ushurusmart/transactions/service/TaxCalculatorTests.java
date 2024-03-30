//package com.emtech.ushurusmart.transactions.service;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.junit.jupiter.api.BeforeEach;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class TaxCalculatorTests {
//
//    private TaxCalculator taxCalculator;
//
//    @BeforeEach
//    void setUp() {
//        taxCalculator = new TaxCalculator();
//    }
//
//    @Test
//    void calculateTaxForVatibleProduct() {
//        double price = 119.0;
//        String productType = "vatible";
//
//        double result = taxCalculator.calculateTax(productType, price);
//
//        assertEquals(16.0, result, 0.001); // 16% of 100.0 is 16.0
//    }
//
//    @Test
//    void calculateTaxForFreeProduct() {
//        double price = 100.0;
//        String productType = "free";
//
//        double result = taxCalculator.calculateTax(productType, price);
//
//        assertEquals(0.0, result, 0.001); // 0% of 100.0 is 0.0
//    }
//
//    @Test
//    void calculateTaxThrowsExceptionForUnknownProductType() {
//        double price = 100.0;
//        String productType = "unknown";
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            taxCalculator.calculateTax(productType, price);
//        });
//    }
//
//}
