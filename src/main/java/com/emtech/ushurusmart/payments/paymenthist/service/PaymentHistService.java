package com.emtech.ushurusmart.payments.paymenthist.service;

import com.emtech.ushurusmart.payments.paymenthist.entity.payment;
import com.emtech.ushurusmart.payments.paymenthist.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentHistService {
@Autowired
private PaymentRepository paymentRepository;
public List<payment> getPaymentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
    return paymentRepository.findByTransactionDateBetween(startDate, endDate);
}

}
