package com.emtech.ushurusmart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class otpconfig {
    // twilio configuration properties
    @Value("${app.twilio.accountSid}")
    public String ACCOUNT_SID;
    @Value("${app.twilio.authToken}")
    public String AUTH_TOKEN;
    @Value("${app.twilio.phoneNumber}")
    public String TWILIO_PHONE_NUMBER;

    @Override
    public String toString() {
        return toString();
    }
}
