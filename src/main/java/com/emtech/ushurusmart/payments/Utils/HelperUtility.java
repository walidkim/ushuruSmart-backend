package com.emtech.ushurusmart.payments.Utils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class HelperUtility {

    public static String toBase64String(String value) {
        byte[] data = value.getBytes(StandardCharsets.ISO_8859_1);
        return Base64.getEncoder().encodeToString(data);
    }
    public static String getTimeStamp(){
        LocalDateTime currentTime=LocalDateTime.now();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedTime=currentTime.format(formatter);

        return formattedTime;
    }

}

