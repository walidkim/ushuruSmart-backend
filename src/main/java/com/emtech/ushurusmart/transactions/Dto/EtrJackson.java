 package com.emtech.ushurusmart.transactions.Dto;

 import java.io.IOException;
 import com.fasterxml.jackson.core.JsonProcessingException;
 import com.fasterxml.jackson.databind.JsonMappingException;
 import com.fasterxml.jackson.databind.ObjectMapper;

 public class EtrJackson {
     public static Transaction parseJson(String json) throws JsonMappingException,
             JsonProcessingException {
         ObjectMapper objectMapper = new ObjectMapper();
         try {
             return objectMapper.readValue(json, Transaction.class);
         } catch (IOException e) {
             e.printStackTrace();
             return null;
         }
     }
 }
