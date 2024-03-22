package com.emtech.ushurusmart.transactions.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.emtech.ushurusmart.transactions.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.emtech.ushurusmart.transactions.Dto.TransactionRequest;
import com.emtech.ushurusmart.transactions.service.TaxCalculator;

@RestController
@RequestMapping("/api/tax")
public class TransactionController {
    @Autowired
    private TaxCalculator taxCalculatorService;

    @PostMapping("/make-transaction")
    public ResponseEntity makeTransaction(@RequestBody TransactionRequest request) {
        try {

            List<TransactionRequest> requests = Arrays.asList(
                    new TransactionRequest("A233333","taxable", 10.0),
                    new TransactionRequest("A233333","free", 20.0),
                    new TransactionRequest("A233333","taxable", 15.0));
            double tax = taxCalculatorService.calculateTax(request.getType(), request.getPrice());

            byte[] invoice = pdfService.generateInvoice(request.getBuyerKRAPin(), requests);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice.pdf");

// Wrap the byte array in a ByteArrayResource
            ByteArrayResource resource = new ByteArrayResource(invoice);

// Return the ResponseEntity with the resource as the body
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Transaction of type '" + request.getType() + "' is not supported.");
        } catch (IOException e) {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }

    @Autowired
    private InvoiceService pdfService;
    @GetMapping("/generate-invoice")
    public ResponseEntity<byte[]> generateInvoice(@RequestParam String buyerPin) {
        List<TransactionRequest> requests = Arrays.asList(
                new TransactionRequest("","taxable", 10.0),
                new TransactionRequest("","free", 20.0),
                new TransactionRequest("","taxable", 15.0));

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
