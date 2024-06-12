package com.emtech.ushurusmart.payments.dtos.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Item {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Value")
    private Object value;

    public String toString() {
        try {
            return (new ObjectMapper()).writeValueAsString(this);
        } catch (Throwable var2) {
            try {
                throw var2;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }

    @JsonProperty("Name")
    public void setName(final String name) {
        this.name = name;
    }

    @JsonProperty("Value")
    public void setValue(final Object value) {
        this.value = value;
    }
}