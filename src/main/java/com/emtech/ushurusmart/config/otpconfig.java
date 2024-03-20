package com.walid.myApp.config;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class config {

    // twilio configuration properties
    public static final String ACCOUNT_SID = "ACc75272f6f52759c2159e57e70326df21";
    public static final String AUTH_TOKEN = "b7671a2445d5b2e116e10ae5d1a67c3d";
    public static final String TWILIO_PHONE_NUMBER = "+12694431149";

    @Override
    public String toString() {
        return toString();
    }
}
