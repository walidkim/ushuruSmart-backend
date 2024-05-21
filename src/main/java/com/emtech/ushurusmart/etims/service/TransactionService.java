package com.emtech.ushurusmart.etims.service;

import com.emtech.ushurusmart.etims.Dtos.controller.SaleDto;
import com.emtech.ushurusmart.etims.Dtos.controller.TransactionDto;
import com.emtech.ushurusmart.etims.entity.Etims;
import com.emtech.ushurusmart.etims.entity.Sale;
import com.emtech.ushurusmart.etims.entity.Transaction;
import com.emtech.ushurusmart.etims.repository.EtimsRepository;
import com.emtech.ushurusmart.etims.repository.SalesRepository;
import com.emtech.ushurusmart.etims.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.xssf.usermodel.*;

import static com.emtech.ushurusmart.utils.service.GeneratorService.generateRandomString;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private com.emtech.ushurusmart.etims.service.TaxCalculator taxCalculator;

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
            double tax = taxCalculator.calculateTax(dto.isTaxable(), dto.getAmount());
            sale.setTax(tax);
            totalTax += tax;

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

    public Double getTaxHistory() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(Transaction::getTax)
                .reduce(0.0, Double::sum);
    }
    public Long getTransactionCount() {
        List<Transaction> transactions = transactionRepository.findAll();
        return (long) transactions.size();
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

    public byte[] generateExcel() throws IOException {
        List<Transaction> transactions = transactionRepository.findAll();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Transaction Report");
        XSSFRow headerRow = sheet.createRow(0);

        // Create a cell style for headers with bold font
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        XSSFCellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);  // Center alignment

        // Create a cell style for borders and justification
        XSSFCellStyle borderCellStyle = workbook.createCellStyle();
        borderCellStyle.setBorderTop(BorderStyle.THIN);
        borderCellStyle.setBorderBottom(BorderStyle.THIN);
        borderCellStyle.setBorderLeft(BorderStyle.THIN);
        borderCellStyle.setBorderRight(BorderStyle.THIN);
        borderCellStyle.setAlignment(HorizontalAlignment.LEFT); // Left alignment for data cells

        // Set headers
        String[] headers = {"ID", "Amount", "Date Created", "INVOICE Number", "TAX"};
        for (int i = 0; i < headers.length; i++) {
            XSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowIndex = 1;
        for (Transaction transaction : transactions) {
            XSSFRow dataRow = sheet.createRow(rowIndex++);
            setCellData(dataRow, 0, transaction.getId(), borderCellStyle);
            setCellData(dataRow, 1, transaction.getAmount(), borderCellStyle);
            setCellData(dataRow, 2, String.valueOf(transaction.getDateCreated()), borderCellStyle);
            setCellData(dataRow, 3, transaction.getInvoiceNumber(), borderCellStyle);
            setCellData(dataRow, 4, transaction.getTax(), borderCellStyle);
        }

        // Autosize columns to fit the content
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }

    private void setCellData(XSSFRow row, int column, Object value, XSSFCellStyle borderCellStyle) {
        if (value!= null) {
            CellStyle style = row.getRowStyle(); // Get the default style for the row
            if (value instanceof Date) { // Check if the value is a Date object
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); // Define the date format
                String dateString = dateFormat.format((Date) value); // Format the date
                row.createCell(column).setCellValue(dateString); // Set the formatted date string
            } else if (value instanceof Double || value instanceof Float) { // Check if the value is a number
                DecimalFormat decimalFormat = new DecimalFormat("#,##0.00"); // Define the number format
                String numberString = decimalFormat.format((Double) value); // Format the number
                row.createCell(column).setCellValue(numberString); // Set the formatted number string
            } else {
                row.createCell(column).setCellValue(value.toString()); // Default case for other types
            }
        }
    }
}
