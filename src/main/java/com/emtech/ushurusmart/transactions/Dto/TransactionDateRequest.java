package com.emtech.ushurusmart.transactions.Dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class TransactionDateRequest {
    private String startDate;
    private String endDate;

}