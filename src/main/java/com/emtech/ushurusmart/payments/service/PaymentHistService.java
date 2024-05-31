package com.emtech.ushurusmart.payments.service;

import com.emtech.ushurusmart.payments.entity.PaymentEntity;
import com.emtech.ushurusmart.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentHistService {
@Autowired
private PaymentRepository paymentRepository;
public List<PaymentEntity> getPaymentsBetweenDates(LocalDate startDate, LocalDate endDate) {
    return paymentRepository.findByTransactionDateBetween(startDate, endDate);
}

}
