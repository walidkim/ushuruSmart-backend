package com.emtech.ushurusmart.utils.controller;

import com.emtech.ushurusmart.config.LoggerSingleton;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
public class Responses extends LoggerSingleton {
    public ResponseEntity<ResContructor> create500Response(Exception e){
        ResContructor res= new ResContructor();
        logger.error(e.getMessage());
        res.setMessage("Something happened. Please try again later.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
}
