package com.emtech.ushurusmart.etrModule.Dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.exceptions.IOException;

public class EtrJackson {
    public static Transaction parseJson(String json) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Transaction myetr = objectMapper.readValue(json, Transaction.class);
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
