package com.emtech.ushurusmart.payments.dtos;

import lombok.Data;

@Data
public class sendPushRequest {

    private String phoneNo;
    private int amount;
    public sendPushRequest(String phoneNo, int amount) {
        this.phoneNo = phoneNo;
        this.amount = amount;
    }



}


