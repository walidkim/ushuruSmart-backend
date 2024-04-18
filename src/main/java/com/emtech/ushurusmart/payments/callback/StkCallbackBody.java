package com.emtech.ushurusmart.payments.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StkCallbackBody {
    @JsonProperty("stkCallback")
    private StkCallBack stkCallback;

}


