package com.emtech.ushurusmart.etims.service;

import com.emtech.ushurusmart.etims.Dtos.controller.SaleDto;
import com.emtech.ushurusmart.etims.Dtos.controller.TransactionDto;
import com.emtech.ushurusmart.etims.entity.Etims;
import com.emtech.ushurusmart.etims.entity.Sale;
import com.emtech.ushurusmart.etims.entity.Transaction;
import com.emtech.ushurusmart.etims.repository.EtimsRepository;
import com.emtech.ushurusmart.etims.repository.SalesRepository;
import com.emtech.ushurusmart.etims.repository.TransactionRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.emtech.ushurusmart.utils.service.GeneratorService.generateRandomString;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private TaxCalculator taxCalculator;

    @Autowired
    private EtimsRepository etimsRepository;

    public Transaction createTransaction(TransactionDto data, Etims owner) {
        Transaction transaction = new Transaction();
        transaction.setEtims(owner);
        transaction.setBuyerPin(data.getBuyerPin());
        double amount = 0.0;
        double totalTax = 0.0;
        List<Sale> sales = new ArrayList<>();
        for (SaleDto dto : data.getSales()) {
            Sale sale = new Sale();
            sale.setName(dto.getName());
            sale.setTaxable(dto.isTaxable());
            double tax= taxCalculator.calculateTax(dto.isTaxable(), dto.getAmount());
            sale.setTax(tax);
            totalTax +=tax;

            sale.setAmount(dto.getAmount());
            salesRepository.save(sale);
            sales.add(sale);
            amount += dto.getAmount();
        }
        transaction.setInvoiceNumber(generateRandomString(20));
        transaction.setTax(totalTax);
        transaction.setSales(sales);
        transaction.setAmount(amount);
        return transaction;
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Double getTransactionHistory() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(Transaction::getAmount)
                .reduce(0.0, Double::sum);
    }
    public Double getTaxHistory(){
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(Transaction::getTax)
                .reduce(0.0,Double::sum);
    }

    @Transactional
    public List<Transaction> getTransactionToday() {
        LocalDate today = LocalDate.now();
        return transactionRepository.findByTransactionDate(today);
    }

    @Transactional
    public List<Transaction> getTransactionsDaily(LocalDate date) {
        return transactionRepository.findByTransactionDate(date);
    }

    @Transactional
    public List<Transaction> getTransactionsMonthly(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();
        return transactionRepository.findByTransactionDateBetween(startDate, endDate);
    }

    public void generateExcel(HttpServletResponse response) throws IOException {

        List<Transaction> etimsTransactionList = transactionRepository.findAll();

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

        for (Transaction etimsTransactions : etimsTransactionList) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(etimsTransactions.getId());
            dataRow.createCell(1).setCellValue(etimsTransactions.getAmount());
            dataRow.createCell(2).setCellValue(String.valueOf(etimsTransactions.getDateCreated()));
            dataRow.createCell(3).setCellValue(etimsTransactions.getEtims().getEtimsCode());
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
