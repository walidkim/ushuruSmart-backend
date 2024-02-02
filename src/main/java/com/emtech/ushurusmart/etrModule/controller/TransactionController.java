package com.emtech.ushurusmart.etrModule.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emtech.ushurusmart.etrModule.entity.EntityResponse;
import com.emtech.ushurusmart.etrModule.entity.TransactionRequest;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @PostMapping("/make-transaction")
    public ResponseEntity<EntityResponse<String>> makeTransaction(@RequestBody TransactionRequest request) {
        EntityResponse<String> response = new EntityResponse<String>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Transaction successful");
        response.setBody(
                "Transaction of type '" + request.getType() + "' with price " + request.getPrice() + " processed.");

        return ResponseEntity.ok(response);
    }
}
