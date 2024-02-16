package com.emtech.ushurusmart.etrModule.entity;


import jakarta.persistence.Id;

import java.time.LocalDateTime;

public class etr {
    @Id
    private double invoiceNumber;
    private double amount;
    private LocalDateTime date;
    private String buyerPin;
//generate getters and setters 
    public double getInvoiceNumber() {
        return invoiceNumber;
    }
    public void setInvoiceNumber(double invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public String getBuyerPin() {
        return buyerPin;
    }
    public void setBuyerPin(String buyerPin) {
        this.buyerPin = buyerPin;
    }
//reading and mapping json file using jackson
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class EtrJackson {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            etr myetr= objectMapper.readValue(new File("etr.json"), etr.class);
            // System.out.println("invoiceNumber: " + myetr.getInvoiceNumber());
            // System.out.println("amount: " + myetr.getAmount());
            // System.out.println("date: " + myetr.getDate());
            // System.out.println("buyerPin: " + myetr.getBuyerPin());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

    

}
