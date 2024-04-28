package com.emtech.ushurusmart.utils.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Responses {
    public static ResponseEntity<ResContructor> create500Response(Exception e){
        ResContructor res= new ResContructor();
        System.out.println(e.getMessage());
        res.setMessage("Something happened. Please try again later.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
}
