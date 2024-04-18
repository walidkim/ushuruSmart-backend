package com.emtech.ushurusmart.payments.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StkCallBack {

    @JsonProperty("MerchantRequestID")
    private String merchantRequestId;

    @JsonProperty("CheckoutRequestID")
    private String checkoutRequestId;

    @JsonProperty("ResultCode")
    private int resultCode;

    @JsonProperty("ResultDesc")
    private String resultDesc;

    @JsonProperty("CallbackMetadata")
    private CallbackMetadata callbackMetadata;

}

