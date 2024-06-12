package com.emtech.ushurusmart.payments.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
@Data
public class failedPushResponse {

    @JsonPropertyDescription("requestId")
    String requestId;
    @JsonPropertyDescription("errorCode")
    String errorCode;
    @JsonPropertyDescription("errorMessage")
    String errorMessage;

    @SneakyThrows
    @Override
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }

}


