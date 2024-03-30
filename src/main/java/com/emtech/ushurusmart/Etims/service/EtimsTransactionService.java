package com.emtech.ushurusmart.Etims.service;

import com.emtech.ushurusmart.Etims.entity.Etims;
import com.emtech.ushurusmart.Etims.entity.Transaction;
import com.emtech.ushurusmart.Etims.repository.EtimsRepository;
import com.emtech.ushurusmart.Etims.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.emtech.ushurusmart.utils.service.GeneratorService.generateRandomString;

@Service
public class EtimsTransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EtimsRepository etimsRepository;

    public Transaction save(Transaction transaction){
        transaction.setInvoiceNumber(generateRandomString(20));
        Etims owner= etimsRepository.findByBusinessOwnerKRAPin(transaction.getOwnerPin()).orElse(null);
        assert owner != null;
        transaction.setEtimsNumber(owner.getEtimsCode());
        return transactionRepository.save(transaction);
    }
}
