package com.emtech.ushurusmart.etrModule.service;
<<<<<<< HEAD
=======

import com.emtech.ushurusmart.etrModule.Dto.TransactionDTO;
import com.emtech.ushurusmart.etrModule.entity.Transaction;
import com.emtech.ushurusmart.etrModule.mapper.TransactionMapper;
>>>>>>> dee7263 (TRansaction Crud)
import com.emtech.ushurusmart.etrModule.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
<<<<<<< HEAD
    private TransactionRepository transactionRepository;

=======
    @Autowired
    private TransactionRepository transactionRepository;

    public void saveTransaction(TransactionDTO dto){
        Transaction entity= TransactionMapper.toEntity(dto);
        transactionRepository.save(entity);;
    }

>>>>>>> dee7263 (TRansaction Crud)
}
