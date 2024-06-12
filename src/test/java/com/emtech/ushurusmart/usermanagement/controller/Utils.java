package com.emtech.ushurusmart.usermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public class Utils {
    public static <T> T parseJsonString(String jsonString, Class<T> valueType) {
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            return objectMapper.readValue(jsonString, valueType);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Return null or handle the exception as needed
        }
    }


}
