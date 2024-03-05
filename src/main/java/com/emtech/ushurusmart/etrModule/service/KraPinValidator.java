package com.emtech.ushurusmart.etrModule.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KraPinValidator {
    private String kraPin;
    private static final String KRA_PIN_REGEX = "^[A-Za-z]{1}[0-9]{9}[A-Za-z]{1}$";
    private static final String API_URL = "https://etims-api-sbx.kra.go.ke/validateKraPin?kraPin=";
    private static final Pattern pattern = Pattern.compile(KRA_PIN_REGEX);


    public String getKraPin() {
        return kraPin;
    }

    public KraPinValidator(String kraPin){
        this.kraPin= kraPin;
    }

    public void setKraPin(String kraPin) {
        if (validateKraPin(kraPin)) {
            this.kraPin = kraPin;
        } else {
            throw new IllegalArgumentException("Invalid KRA PIN format");
        }
    }

    private boolean validateKraPin(String kraPin) {
        Matcher matcher = pattern.matcher(kraPin);
        return matcher.matches();
    }

}
