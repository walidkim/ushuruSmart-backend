package com.emtech.ushurusmart.payments.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
@Data
public class okPushResponse {
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
    @Override
    @SneakyThrows
    public String toString(){
        return new ObjectMapper().writeValueAsString(this);
    }

}


