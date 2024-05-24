package com.emtech.ushurusmart.payments.service;

import com.emtech.ushurusmart.payments.entity.PaymentEntity;

import java.time.LocalDate;
import java.util.List;

public interface PaymentService {

    List<PaymentEntity> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate);
    double calculateTotalTax(LocalDate start, LocalDate end);
}
