package com.emtech.ushurusmart.config;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class otpconfig {

    // twilio configuration properties
    public static final String ACCOUNT_SID = "ACc26c8aa04fa5255e88000124087eaee0";
    public static final String AUTH_TOKEN = "f7f93b9cb4b4aff3a790a1803b076cf5";
    public static final String TWILIO_PHONE_NUMBER = "+12515721303";

    @Override
    public String toString() {
        return toString();
    }
}
