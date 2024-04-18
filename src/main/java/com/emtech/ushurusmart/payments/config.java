package com.emtech.ushurusmart.payments;

public class config {
//Mpesa Credentials
    public static String authURL=" https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";
    public static String keySecret="c6TuFwoSjJy5d9IUe3bNBxxImoD2YBTFoV2e3ui7XGiFzbWf:FGlhiBK2QvtumfRp6rEFKmZ9Yv4tvj02QgC0P4Dj0TjPjqdPPErfplbgzjXLhcti";
    public static String accessToken;
    public static String shortCode="174379";
    public static String passkey="bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    public static String stkPushURL=" https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest";
    public static String callBackURL="https://artistic-skunk-curiously.ngrok-free.app/callback";

}
