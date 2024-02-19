package com.emtech.ushurusmart.etrModule.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emtech.ushurusmart.etrModule.entity.EtrJackson;

@Service
public class etimsRequests {
    private final String kra_endpoint= "https://etims-api-sbx.kra.go.ke";

    @Autowired
    private EtrJackson etrJackson = new EtrJackson();

    

    
}

