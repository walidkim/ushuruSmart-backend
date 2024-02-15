package com.emtech.ushurusmart.etrModule.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import com.emtech.ushurusmart.UshuruSmartApplication;
import com.emtech.ushurusmart.etrModule.entity.TransactionRequest;
import com.emtech.ushurusmart.etrModule.service.InvoiceService;

@SpringBootTest
@ContextConfiguration(classes = UshuruSmartApplication.class)
public class TransactionControllerTest {

    @Autowired
    private TransactionController invoiceController;

    @Autowired
    private InvoiceService pdfService;

    @Test
    public void testGenerateInvoiceSuccess() throws Exception {
        String buyerName = "John Doe";
        List<TransactionRequest> requests = Arrays.asList(
                new TransactionRequest("TransactionRequest 1", 10.0),
                new TransactionRequest("TransactionRequest 2", 20.0),
                new TransactionRequest("TransactionRequest 3", 15.0));

        byte[] expectedInvoice = new byte[100]; // Replace with actual expected content
        when(pdfService.generateInvoice(buyerName, requests)).thenReturn(expectedInvoice);

        ResponseEntity<byte[]> response = invoiceController.generateInvoice(buyerName);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_PDF);
        assertEquals(response.getHeaders().getContentDisposition().getFilename(), "invoice.pdf");
        assertEquals(response.getBody(), expectedInvoice);
    }
}
