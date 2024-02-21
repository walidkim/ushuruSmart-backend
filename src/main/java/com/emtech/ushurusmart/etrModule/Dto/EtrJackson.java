package com.emtech.ushurusmart.etrModule.entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class EtrJackson {
    public static Transaction parseJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Transaction myetr= objectMapper.readValue(json, Transaction.class);
            myetr.getInvoiceNumber();
            myetr.getAmount();
            myetr.getDate();
            myetr.getBuyerPin();

            return myetr;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
