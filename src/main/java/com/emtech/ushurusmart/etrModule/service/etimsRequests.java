package com.emtech.ushurusmart.etrModule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.emtech.ushurusmart.etrModule.entity.EtrJackson;
import com.emtech.ushurusmart.etrModule.entity.Transaction;

@Service
public class etimsRequests {
    private final String kra_endpoint= "https://etims-api-sbx.kra.go.ke";
    Transaction trans= EtrJackson.parseJson(json"");
    if(trans != null){
        trans.getBuyerPin();
    }


}

