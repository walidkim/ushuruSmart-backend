package com.emtech.ushurusmart.repository;

<<<<<<< HEAD
=======
import com.emtech.ushurusmart.etrModule.entity.Product;
>>>>>>> dee7263 (TRansaction Crud)
import com.emtech.ushurusmart.etrModule.entity.Transaction;
import com.emtech.ushurusmart.etrModule.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
<<<<<<< HEAD
=======

import java.math.BigDecimal;
>>>>>>> dee7263 (TRansaction Crud)
import java.util.List;

@SpringBootTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
<<<<<<< HEAD
    public void save(){
        Transaction transaction = new Transaction();

        transaction.setAmount(37000);
        transaction.setBuyerPin("P125193278L");

        Transaction savedObject=transactionRepository.save(transaction);
    }
    @Test
    void saveAll(){
        Transaction transaction = new Transaction();

        transaction.setAmount(540000);
=======
    public void saveMethod(){
        Transaction transaction = new Transaction();

        transaction.setAmount(new BigDecimal(300000));
        transaction.setBuyerPin("A011193278C");
        Transaction savedObject=transactionRepository.save(transaction);
    }
    @Test
    public void saveAllMethod(){
        Transaction transaction = new Transaction();

        transaction.setAmount(new BigDecimal(540000));
>>>>>>> dee7263 (TRansaction Crud)
        transaction.setBuyerPin("P012197645F");

        Transaction transaction1 = new Transaction();

<<<<<<< HEAD
        transaction1.setAmount(42000);
=======
        transaction1.setAmount(new BigDecimal(42000));
>>>>>>> dee7263 (TRansaction Crud)
        transaction1.setBuyerPin("P034193278Z");

        Transaction transaction2 = new Transaction();

<<<<<<< HEAD
        transaction2.setAmount(50000);
=======
        transaction2.setAmount(new BigDecimal(50000));
>>>>>>> dee7263 (TRansaction Crud)
        transaction2.setBuyerPin("P201193256K");

        Transaction transaction3 = new Transaction();

<<<<<<< HEAD
        transaction3.setAmount(170000);
=======
        transaction3.setAmount(new BigDecimal(170000));
>>>>>>> dee7263 (TRansaction Crud)
        transaction3.setBuyerPin("A011193278C");

        transactionRepository.saveAll(List.of(transaction,transaction1,transaction2,transaction3));
    }
    @Test
<<<<<<< HEAD
    public void findById(){
=======
    public void findByIDMethod(){
>>>>>>> dee7263 (TRansaction Crud)
        Long id=4L;
        Transaction transaction= transactionRepository.findById(id).get();
        System.out.println(transaction.getBuyerPin());
    }
    @Test
<<<<<<< HEAD
    public void findAll(){
=======
    public void findAllMethod(){
>>>>>>> dee7263 (TRansaction Crud)
        List<Transaction>transaction=transactionRepository.findAll();
        transaction.forEach((t)->{System.out.println(t.getBuyerPin());});
    }
    @Test
<<<<<<< HEAD
    public void update() {
        Long id = 2L;
        Transaction transaction = transactionRepository.findById(id).get();

        transaction.setAmount(450000);
        transactionRepository.save(transaction);
    }
    @Test
    public void deleteById(){
=======
    public void updateMethod() {
        Long id = 2L;
        Transaction transaction = transactionRepository.findById(id).get();

        transaction.setAmount(new BigDecimal(450000));
        transactionRepository.save(transaction);
    }
    @Test
    public void deleteByIdMethod(){
>>>>>>> dee7263 (TRansaction Crud)
        Long id=5L;
        transactionRepository.deleteById(id);
    }
    @Test
<<<<<<< HEAD
    public void deleteAll(){

        transactionRepository.deleteAll();
    }
    @Test
    public void count(){
        long count = transactionRepository.count();
=======
    public void deleteAllByIdMethod(){
        Transaction transaction= transactionRepository.findById(4L).get();
        Transaction transaction1= transactionRepository.findById(6L).get();

        transactionRepository.deleteAll(List.of(transaction,transaction1));
    }
    @Test
    public void deleteAllMethod(){
        transactionRepository.deleteAll();
    }
    @Test
    public void countMethod(){
        long count = transactionRepository.count();

>>>>>>> dee7263 (TRansaction Crud)
        System.out.println(count);
    }
}
