package com.emtech.ushurusmart.Etims.factory;

import com.emtech.ushurusmart.Etims.Dtos.controller.TransactionDto;
import com.emtech.ushurusmart.Etims.entity.EtimsTransaction;

public class EntityFactory {
    public static EtimsTransaction createTransaction(TransactionDto data) {
        EtimsTransaction transaction = new EtimsTransaction();
        transaction.setOwnerPin(data.getOwnerPin());
        transaction.setTaxable(data.isTaxable());
        transaction.setBuyerPin(data.getBuyerPin());
        transaction.setAmount(data.getAmount());
        return transaction;
    }
}
