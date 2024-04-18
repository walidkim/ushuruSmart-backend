package com.emtech.ushurusmart.payments.dtos.callback;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CallbackMetadata {

    @JsonProperty("Item")
    private List<Item> items;

    // Getters and setters
}

