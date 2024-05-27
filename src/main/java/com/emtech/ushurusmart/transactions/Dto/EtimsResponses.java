package com.emtech.ushurusmart.transactions.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class EtimsResponses {
    public static TransactionResponse parseMakeTransactionResponse(String jsonString) throws JsonProcessingException {
        return new ObjectMapper().readValue(jsonString, TransactionResponse.class);
    }

    @Data
    public static class TransactionResponse {

        @JsonProperty("message")
        private String message;

        @JsonProperty("data")
        private TransactionData data;

        @Data
        public static class TransactionData {

            @JsonProperty("id")
            private int id;

            @JsonProperty("amount")
            private double amount;


            @JsonProperty("date")
            private LocalDateTime date;

            @JsonProperty("buyerPin")
            private String buyerPin;

            @JsonProperty("invoiceNumber")
            private String invoiceNumber;

            @JsonProperty("tax")
            private double tax;

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            @JsonProperty("dateCreated")
            private String dateCreated;

            @JsonProperty("sales")
            private List<Sale> sales;
            @JsonProperty("etims")
            private EtimsData etims;
        }
        @Data
        public static class EtimsData {

            @JsonProperty("id")
            private int id;

            @JsonProperty("businessKRAPin")
            private String businessKRAPin;

            @JsonProperty("businessOwnerKRAPin")
            private String businessOwnerKRAPin;


            @JsonProperty("name")
            private String name;


            @JsonProperty("etimsCode")
            private String etimsCode;

            @JsonProperty("businessName")
            private String businessName;

            @JsonProperty("transactions")
            private Object transactions;
        }

        @Data
        public static class Sale {

            @JsonProperty("id")
            private int id;

            @JsonProperty("taxable")
            private boolean taxable;

            @JsonProperty("tax")
            private double tax;

            @JsonProperty("name")
            private String name;

            @JsonProperty("amount")
            private double amount;
        }
    }
}


