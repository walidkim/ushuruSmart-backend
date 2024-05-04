package com.emtech.ushurusmart.payments.dtos.callback;

import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CallbackMetadata {
    @JsonProperty("Item")
    private List<Item> items;

    public CallbackMetadata() {
    }

    public String getItemValueByName(String name) {
        Iterator var3 = this.items.iterator();

        while (var3.hasNext()) {
            Item item = (Item) var3.next();
            if (item.getName().equals(name)) {
                return item.getValue().toString();
            }
        }

        return null;
    }
}
