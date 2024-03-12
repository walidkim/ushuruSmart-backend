package com.emtech.ushurusmart.repository;

import com.emtech.ushurusmart.etrModule.entity.Transaction;
import com.emtech.ushurusmart.etrModule.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
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
        transaction.setBuyerPin("P012197645F");

        Transaction transaction1 = new Transaction();

        transaction1.setAmount(42000);
        transaction1.setBuyerPin("P034193278Z");

        Transaction transaction2 = new Transaction();

        transaction2.setAmount(50000);
        transaction2.setBuyerPin("P201193256K");

        Transaction transaction3 = new Transaction();

        transaction3.setAmount(170000);
        transaction3.setBuyerPin("A011193278C");

        transactionRepository.saveAll(List.of(transaction,transaction1,transaction2,transaction3));
    }
    @Test
    public void findById(){
        Long id=4L;
        Transaction transaction= transactionRepository.findById(id).get();
        System.out.println(transaction.getBuyerPin());
    }
    @Test
    public void findAll(){
        List<Transaction>transaction=transactionRepository.findAll();
        transaction.forEach((t)->{System.out.println(t.getBuyerPin());});
    }
    @Test
    public void update() {
        Long id = 2L;
        Transaction transaction = transactionRepository.findById(id).get();

        transaction.setAmount(450000);
        transactionRepository.save(transaction);
    }
    @Test
    public void deleteById(){
        Long id=5L;
        transactionRepository.deleteById(id);
    }
    @Test
    public void deleteAll(){

        transactionRepository.deleteAll();
    }
    @Test
    public void count(){
        long count = transactionRepository.count();
        System.out.println(count);
    }
}
