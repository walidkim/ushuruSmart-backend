package com.emtech.ushurusmart.etrModule.controller;

import com.emtech.ushurusmart.etrModule.Dto.TransactionRequest;
import com.emtech.ushurusmart.etrModule.service.InvoiceService;
import com.emtech.ushurusmart.etrModule.service.TaxCalculator;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/tax")
@Tag(name ="Transaction")
public class TransactionController {
    @Autowired
    private TaxCalculator taxCalculatorService;

    @PostMapping("/make-transaction")
    public ResponseEntity makeTransaction(@RequestBody TransactionRequest request) {
        try {
            double tax = taxCalculatorService.calculateTax(request.getType(), request.getPrice());
            return ResponseEntity.ok()
                    .body("Transaction of type '" + request.getType() + " with tax "
                            + tax + " of value" + request.getPrice() + " processed.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Transaction of type '" + request.getType() + "' is not supported.");
        }
    }

    @Autowired
    private InvoiceService pdfService;
    @GetMapping("/generate-invoice")
    public ResponseEntity<byte[]> generateInvoice(@RequestParam String buyerPin) {
        List<TransactionRequest> requests = Arrays.asList(
                new TransactionRequest("taxable", 10.0),
                new TransactionRequest("free", 20.0),
                new TransactionRequest("taxable", 15.0));

        try {
            byte[] invoice = pdfService.generateInvoice(buyerPin, requests);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "invoice.pdf");

            return new ResponseEntity<>(invoice, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
