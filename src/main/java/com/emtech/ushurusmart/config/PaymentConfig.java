package com.emtech.ushurusmart.config;

import org.springframework.beans.factory.annotation.Value;

public class PaymentConfig {
    // Mpesa Credentials
    public static String authURL = "https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";
    @Value("${app.mpesa.keySecret}")
    public static String keySecret;
    public static String accessToken;
    @Value("${app.mpesa.shortCode}")
    public static String shortCode;
    @Value("${app.mpesa.passkey}")
    public static String passkey;
    public static String stkPushURL = " https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest";
    @Value("${app.mpesa.callbackURL}")
    public static String callBackURL;

}
