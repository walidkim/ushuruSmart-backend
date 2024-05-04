package com.emtech.ushurusmart.payments.Utils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class HelperUtility {
    public HelperUtility() {
    }

    public static String toBase64String(String value) {
        byte[] data = value.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(data);
    }

    public static LocalDateTime getLocalDateTime(String timestamp) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        // Parse the string into a LocalDateTime object
        LocalDateTime localDateTime = LocalDateTime.parse(timestamp, formatter);

        return localDateTime;
    }

    public static String getTimeStamp() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedTime = currentTime.format(formatter);
        return formattedTime;
    }
}
