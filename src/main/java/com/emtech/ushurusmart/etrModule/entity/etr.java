package com.emtech.ushurusmart.etrModule.entity;


import jakarta.persistence.Id;

import java.time.LocalDateTime;

public class etr {
    @Id
    private double invoiceNumber;
    private double amount;
    private LocalDateTime date;

    private String buyerPin;

}
