package com.emtech.ushurusmart.transactions.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.emtech.ushurusmart.transactions.entity.Transaction;
import com.emtech.ushurusmart.transactions.repository.TransactionRepository;

@Service
public class ReportService {
    @Autowired
    TransactionRepository transactionRepository;

    ReportService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public ResponseEntity<byte[]> writeTransactionsToExcel(String startDate, String endDate) throws IOException {

        List<Transaction> transactions = transactionRepository.findByDateCreatedBetween(
                startDateConverter(startDate),
                endDateConverter(endDate));

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Transaction Report");

            // Create header row
            XSSFRow headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Invoice Number");
            headerRow.createCell(1).setCellValue("Amount");
            headerRow.createCell(2).setCellValue("Date");
            headerRow.createCell(3).setCellValue("Buyer PIN");

            // Populate data rows
            int rowNum = 1;
            for (Transaction transaction : transactions) {
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(transaction.getInvoiceNumber());
                row.createCell(1).setCellValue(transaction.getAmount());
                row.createCell(2).setCellValue(transaction.getDate().toString());
                row.createCell(3).setCellValue(transaction.getBuyerPin());
            }

            // Write workbook to ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "TransactionReport.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            System.out.println("Excelreport failed: " + e);
            // Handle exception appropriately
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    // Converts start/end dates to LocalDateTime type
    public LocalDateTime startDateConverter(String startDate) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate StartDate = LocalDate.parse(startDate, dateFormatter);

        // Convert LocalDate to LocalDateTime by specifying the time part
        LocalDateTime startDateTime = StartDate.atStartOfDay();

        // Getters and setters

        return startDateTime;
    }

    public LocalDateTime endDateConverter(String endDate) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate EndDate = LocalDate.parse(endDate, dateFormatter);

        // Convert LocalDate to LocalDateTime by specifying the time part
        LocalDateTime endDateTime = EndDate.atTime(LocalTime.MAX);

        // Getters and setters

        return endDateTime;
    }
}
