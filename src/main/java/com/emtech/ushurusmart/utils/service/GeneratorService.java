package com.emtech.ushurusmart.utils.service;

import java.security.SecureRandom;

public class GeneratorService {
    private static final String CAPITALS= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String CHARACTERS = "0123456789" + CAPITALS;

    public static String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 1; i <= length; i++) {

                int randomIndex = random.nextInt(CHARACTERS.length());
                sb.append(CHARACTERS.charAt(randomIndex));
                if(i%4==0 && i<length-1){
                    sb.append("-");
                }

        }
        return sb.toString();
    }
}
