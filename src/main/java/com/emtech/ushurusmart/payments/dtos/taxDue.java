package com.emtech.ushurusmart.payments.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class taxDue {

    private LocalDate startDate;
    private LocalDate endDate;




    public taxDue(LocalDate transactionDate, String amount) {
        this.endDate = endDate;

    }

}
