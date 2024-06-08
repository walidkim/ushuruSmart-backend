package com.emtech.ushurusmart.payments.service;

import com.emtech.ushurusmart.etims.service.TransactionService;
import com.emtech.ushurusmart.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;

@Service
public class EslipService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(EslipService.class);
    @Autowired
    private PaymentRepository eSlipRepository; // Assuming there is a repository for ESLIP related operations
    @Autowired
    private  TransactionService transactionService;
    public String createESlipFromDB() {

        double rawAmount= transactionService.getTaxHistory();

        int amount;
        if (rawAmount >= 1.0) { // Ensure that rawAmount is greater than or equal to 1.0
            amount = (int) Math.round(rawAmount); // Round up the double value and cast it to an int
        } else {
            amount = 0;
        }

        // Generate ESLIP with the calculated amount
        String eslip = generateESlip(amount);

        // Log amount and return ESLIP;
        return (eslip + ":" + Integer.toString(amount));
    }

    private String generateESlip(int amount) {
        // Get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Format the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDateTime = currentDateTime.format(formatter);

        // Convert the amount to hexadecimal
        String hexAmount = Integer.toHexString(amount);

        // Pad the hexadecimal amount with leading zeros to make it a fixed length
        int desiredLength = 4;
        String paddedHexAmount = String.format("%" + desiredLength + "s", hexAmount).replace(' ', '0');

        // Combine the formatted date and time with the padded hexadecimal amount
        String eslip = formattedDateTime + paddedHexAmount;

        // Log the constructed ESLIP number
        return eslip;
    }
}
