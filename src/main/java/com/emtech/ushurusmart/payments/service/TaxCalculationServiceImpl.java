package com.emtech.ushurusmart.payments.service;


import com.emtech.ushurusmart.payments.entity.PaymentEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaxCalculationServiceImpl extends TaxCalculationService{
    @Override
    public List<PaymentEntity> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return List.of();
    }
}
