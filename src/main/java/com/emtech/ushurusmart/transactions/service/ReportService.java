package com.emtech.ushurusmart.transactions.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emtech.ushurusmart.transactions.Dto.Transaction;
import com.emtech.ushurusmart.transactions.repository.TransactionRepository;

@Service
public class ReportService {

    @Autowired
    private TransactionRepository transactionRepository;

    public void writeTransactionsToExcel(List<Transaction> transactions) throws IOException {
        XSSFRow row;
        XSSFSheet sheet;

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            sheet = workbook.createSheet("Transaction Report");

            // Create header row
            XSSFRow headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Invoice Number");
            headerRow.createCell(1).setCellValue("Amount");
            headerRow.createCell(2).setCellValue("Date");
            headerRow.createCell(3).setCellValue("Buyer PIN");

            // Populate data rows
            int rowNum = 1;
            for (Transaction transaction : transactions) {
                row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(transaction.getInvoiceNumber());
                row.createCell(1).setCellValue(transaction.getAmount());
                row.createCell(2).setCellValue(transaction.getDate().toString());
                row.createCell(3).setCellValue(transaction.getBuyerPin());
            }

            // Write to Excel file
            try (FileOutputStream fileOut = new FileOutputStream("TransactionReport.xlsx")) {
                workbook.write(fileOut);
            }
            workbook.close();
        } catch (Exception e) {
            System.out.println("Excelreport failed: " + e);
        }
    }
}
