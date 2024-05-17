package com.emtech.ushurusmart.transactions.controller;


import com.emtech.ushurusmart.config.LoggerSingleton;
import com.emtech.ushurusmart.etims.entity.Transaction;
import com.emtech.ushurusmart.etims.service.TaxCalculationService;
import com.emtech.ushurusmart.etims_middleware.TransactionMiddleware;
import com.emtech.ushurusmart.transactions.Dto.*;
import com.emtech.ushurusmart.transactions.entity.Product;
import com.emtech.ushurusmart.transactions.service.InvoiceService;
import com.emtech.ushurusmart.transactions.service.ProductService;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.service.AssistantService;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.usermanagement.utils.AuthUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api/v1/tax")
public class TaxController  extends LoggerSingleton {

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private TransactionMiddleware transactionMiddleware;

    @Autowired
    private OwnerService ownerService;


    @Autowired
    private AssistantService assistantService;

    @Autowired
    private ProductService productService;

    @Autowired
    TaxCalculationService taxCalculationService;

    @GetMapping("/{startDate}/{endDate}")
    public double getTotalTax(@PathVariable LocalDate startDate, @PathVariable LocalDate endDate) {
        return taxCalculationService.calculateTotalTax(startDate, endDate);
    }

//    @Autowired
//    private JasperPDFService jasperPDFService;

    @GetMapping("/range")
    public ResponseEntity<?> getTransactionsMonthly(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        System.out.println(startDate + " " + endDate);
        ResponseEntity<?> response= transactionMiddleware.getRangeTransactions(startDate, endDate);

        logger.info(response.toString());
        return response;
    }
    @PostMapping("/make-transaction")
    public ResponseEntity<?> makeTransaction(@RequestBody TransactionRequest request, HttpServletResponse responses) throws IOException, JRException {

        String email= AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner= ownerService.findByEmail(email);
        if(owner==null){
            owner= assistantService.findByEmail(email).getOwner();
        }

        EtimsTransactionDto etimReq = generateEtimsRequest(request, owner);


        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(etimReq);


        ResponseEntity<?> response = transactionMiddleware.makeTransaction(jsonString);

        logger.info(response.toString());


        if (response.getStatusCode().value()== HttpStatus.CREATED.value()) {
            System.out.println(response);
            EtimsResponses.TransactionResponse transactionResponse = EtimsResponses.parseMakeTransactionResponse(Objects.requireNonNull(response.getBody()).toString());

            byte[] reportArrayOutputStream = invoiceService.generateInvoice(transactionResponse.getData(), request);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice.pdf");

            //jasperPDFService.exportJasperReport(request, transactionResponse.getData());

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_PDF);
            responseHeaders.setContentLength(reportArrayOutputStream.length);
            responseHeaders.setContentDispositionFormData("attachment", "receipt.pdf");
            return new ResponseEntity<>(reportArrayOutputStream, responseHeaders, HttpStatus.CREATED);

        }
        else{
          return response;
        }


//        if(response.getStatusCode()==HttpStatus.NOT_FOUND){
//            return response;
//        }
//        TransactionData parsed= new TransactionData(Objects.requireNonNull(response.getBody()).getData().toString());
//
//
//

//        TransactionResponse transactionResponse = objectMapper.readValue(response.getBody().toString(), TransactionResponse.class);
//        System.out.println(transactionResponse.toString());
//
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("attachment", "invoice.pdf");
//        System.out.println("Generated PDF");
//
//        ByteArrayOutputStream reportArrayOutputStream = jasperPDFService.exportJasperReport(responses, request.getBuyerKRAPin(), reportProducts, null);
//        System.out.println("Generated output Stream");
//        byte[] reportBytes = reportArrayOutputStream.toByteArray();
//        reportArrayOutputStream.close();
//
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.setContentType(MediaType.APPLICATION_PDF);
//        responseHeaders.setContentLength(reportBytes.length);
//        responseHeaders.setContentDispositionFormData("attachment", "receipt.pdf");


    }

    @NotNull
    private EtimsTransactionDto generateEtimsRequest(TransactionRequest request, Owner owner) {

        EtimsTransactionDto etimReq = new EtimsTransactionDto();
        etimReq.setOwnerPin(owner.getKRAPin());
        etimReq.setBussinessPin(owner.getBusinessKRAPin());
        etimReq.setBuyerPin(request.getBuyerKRAPin());
        List<EtimsProduct> etimsSales = new ArrayList<>();

        for (TransactionProduct sold : request.getSales()) {
            Product product = productService.findById(sold.getProductId());
            double amount = product.getUnitPrice() * sold.getQuantity();
            EtimsProduct etimsProduct = new EtimsProduct();
            etimsProduct.setTaxable(product.isTaxable());
            etimsProduct.setAmount(amount);
            etimsProduct.setName(product.getName());
            etimsSales.add(etimsProduct);
        }
        etimReq.setSales(etimsSales);
        return etimReq;
    }



}