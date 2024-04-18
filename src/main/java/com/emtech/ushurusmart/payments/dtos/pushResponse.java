package com.emtech.ushurusmart.payments.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
@Data
public class pushResponse {
    @JsonProperty("MerchantRequestID")
    private String MerchantRequestID;
    @JsonProperty("CheckoutRequestID")
    private String CheckoutRequestID;
    @JsonProperty("ResponseCode")
    private String ResponseCode;
    @JsonProperty("ResponseDescription")
    private String ResponseDescription;
    @JsonProperty("CustomerMessage")
    private String CustomerMessage;
    @JsonProperty("requestId")
    private String requestId;

    @SneakyThrows
    @Override
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }
}

