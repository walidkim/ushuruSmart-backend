package com.emtech.ushurusmart.config;

import com.emtech.ushurusmart.payments.entity.PaymentEntity;
import com.emtech.ushurusmart.payments.service.TaxCalculationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Component
public class PaymentConfig {

    // Mpesa Credentials
    public static String authURL = "https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";
    @Value("${app.mpesa.keySecret}")
    public String keySecret;
    @Value("${app.mpesa.shortCode}")
    public String shortCode;
    @Value("${app.mpesa.passkey}")
    public String passkey;
    @Value("${app.mpesa.stkPushURL}")
    public String stkPushURL;
    @Value("${app.mpesa.callbackURL}")
    public String callBackURL;


}
