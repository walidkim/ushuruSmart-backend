package com.emtech.ushurusmart.Etims.service;

import com.emtech.ushurusmart.Etims.entity.Etims;
import com.emtech.ushurusmart.Etims.entity.EtimsTransaction;
import com.emtech.ushurusmart.Etims.repository.EtimsRepository;
import com.emtech.ushurusmart.Etims.repository.TransactionRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.emtech.ushurusmart.utils.service.GeneratorService.generateRandomString;

@Service
public class EtimsTransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EtimsRepository etimsRepository;

    public EtimsTransaction save(EtimsTransaction transaction) {
        transaction.setInvoiceNumber(generateRandomString(20));
        Etims owner = etimsRepository.findByBusinessOwnerKRAPin(transaction.getOwnerPin()).orElse(null);
        assert owner != null;
        transaction.setEtimsNumber(owner.getEtimsCode());
        return transactionRepository.save(transaction);
    }
    public Double getTransactionHistory(){
        List<EtimsTransaction> transactions = transactionRepository.findAll();
        double total = transactions.stream()
                .map(EtimsTransaction::getAmount)
                .reduce(0.0, Double::sum);
        return total;
    }
    @Transactional
    public List<EtimsTransaction> getTransactionsDaily(LocalDate date){
        return transactionRepository.findByTransactionDate(date);
    }
    @Transactional
    public List<EtimsTransaction> getTransactionsMonthly(LocalDate startDate, LocalDate endDate){
        return transactionRepository.findByTransactionDateBetween(startDate, endDate);
    }


    public void generateExcel(HttpServletResponse response) throws IOException {

        List<EtimsTransaction> etimsTransactionList = transactionRepository.findAll();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Transaction Report");
        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("Amount");
        row.createCell(2).setCellValue("Date Created");
        row.createCell(3).setCellValue("ETIMS Number");
        row.createCell(4).setCellValue("INVOICE Number");
        row.createCell(5).setCellValue("TAX");

        int dataRowIndex = 1;

        for(EtimsTransaction etimsTransactions: etimsTransactionList) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(etimsTransactions.getId());
            dataRow.createCell(1).setCellValue(etimsTransactions.getAmount());
            dataRow.createCell(2).setCellValue(String.valueOf(etimsTransactions.getDateCreated()));
            dataRow.createCell(3).setCellValue(etimsTransactions.getEtimsNumber());
            dataRow.createCell(4).setCellValue(etimsTransactions.getInvoiceNumber());
            dataRow.createCell(5).setCellValue(etimsTransactions.getTax());
            dataRowIndex++;

        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
