package com.emtech.ushurusmart.etrModule.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.emtech.ushurusmart.etrModule.Repository.KraPinHolder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class kraPinValidator implements KraPinHolder {
    private String kraPin;
    private static final String KRA_PIN_REGEX = "^[A-Za-z]{1}[0-9]{9}[A-Za-z]{1}$";
    private static final String API_URL = "https://etims-api-sbx.kra.go.ke/validateKraPin?kraPin=";
    private static final Pattern pattern = Pattern.compile(KRA_PIN_REGEX);

    @Override
    public String getKraPin() {
        return kraPin;
    }

    @Override
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

    public static void main(String[] args) {
        kraPinValidator validator = new kraPinValidator();

        // Valid KRA PIN
        validator.setKraPin("A123456789Z");
        System.out.println("Valid KRA PIN: " + validator.getKraPin());

        // Invalid KRA PIN (throws exception)
        try {
            validator.setKraPin("1234567890");
        } catch (IllegalArgumentException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
