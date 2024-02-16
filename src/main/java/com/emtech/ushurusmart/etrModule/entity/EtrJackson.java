package com.emtech.ushurusmart.etrModule.entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class EtrJackson {
    public static Transaction parseJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Transaction myetr= objectMapper.readValue(json, Transaction.class);
            // System.out.println("invoiceNumber: " + myetr.getInvoiceNumber());
            // System.out.println("amount: " + myetr.getAmount());
            // System.out.println("date: " + myetr.getDate());
            // System.out.println("buyerPin: " + myetr.getBuyerPin());

            return myetr;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
