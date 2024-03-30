package com.emtech.ushurusmart.Etims.factory;

import com.emtech.ushurusmart.Etims.Dtos.controller.TransactionDto;
import com.emtech.ushurusmart.Etims.entity.Transaction;

public class EntityFactory {
    public static Transaction createTransaction(TransactionDto data){
        Transaction transaction= new Transaction();
        transaction.setOwnerPin(data.getOwnerPin());
        transaction.setTaxable(data.isTaxable());
        transaction.setBuyerPin(data.getBuyerPin());
        transaction.setAmount(data.getAmount());
        return transaction;
    }
}
