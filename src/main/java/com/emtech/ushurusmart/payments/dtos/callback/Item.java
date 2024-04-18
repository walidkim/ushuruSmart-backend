package com.emtech.ushurusmart.payments.dtos.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
@Data
public class Item {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Value")
    private Object value;

    @SneakyThrows
    @Override
    public String toString() {

        return new ObjectMapper().writeValueAsString(this);
    }
    // Getters and setters
}

