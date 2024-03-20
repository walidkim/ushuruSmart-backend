package com.emtech.ushurusmart.usermanagement.controller;

public class HelperUtil {

    public static String capitalizeFirst(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

}
