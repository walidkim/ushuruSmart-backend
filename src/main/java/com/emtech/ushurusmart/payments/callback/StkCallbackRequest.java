package com.emtech.ushurusmart.payments.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
@Data
public class StkCallbackRequest {


    @JsonProperty("Body")
    private StkCallbackBody body;

    @SneakyThrows
    @Override
    public String toString() {

        return new ObjectMapper().writeValueAsString(this);
    }

}
