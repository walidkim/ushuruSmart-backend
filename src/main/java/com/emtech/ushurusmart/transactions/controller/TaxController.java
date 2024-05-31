package com.emtech.ushurusmart.transactions.controller;


import com.emtech.ushurusmart.config.LoggerSingleton;
import com.emtech.ushurusmart.etims_middleware.TransactionMiddleware;
import com.emtech.ushurusmart.transactions.Dto.*;
import com.emtech.ushurusmart.transactions.entity.Product;
import com.emtech.ushurusmart.transactions.service.InvoiceService;
import com.emtech.ushurusmart.transactions.service.JasperPDFService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
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
    private JasperPDFService jasperPDFService;

    @PostMapping("/make-jasperPdf-transaction")
    public ResponseEntity<?> makeJasperTransaction(@RequestBody TransactionRequest request, HttpServletResponse responses) throws IOException, JRException, SQLException {
        String email = AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner = ownerService.findByEmail(email);
        if (owner == null){
            owner = assistantService.findByEmail(email).getOwner();
        }
        EtimsTransactionDto etimsTransactionDto = generateEtimsRequest(request, owner);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(etimsTransactionDto);

        ResponseEntity<?> response = transactionMiddleware.makeTransaction(jsonString);
        logger.info(response.toString());

        if (response.getStatusCode().value() == HttpStatus.CREATED.value()) {
            System.out.println(response);
            EtimsResponses.TransactionResponse transactionResponse = EtimsResponses.parseMakeTransactionResponse(Objects.requireNonNull(response.getBody()).toString());
            byte[] reportArrayOutputStream = jasperPDFService.ownermakeGenerateReport(transactionResponse.getData(), request).toByteArray();
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_PDF);
            header.setContentDispositionFormData("attachment", "receipt.pdf");
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_PDF);
            responseHeaders.setContentLength(reportArrayOutputStream.length);
            responseHeaders.setContentDispositionFormData("attachment", "receipt.pdf");
            return new ResponseEntity<>(reportArrayOutputStream, responseHeaders, HttpStatus.CREATED);
        }
        else {
            return response;
        }
    }


    @PostMapping("/make-transaction")
    public ResponseEntity<?> makeTransaction(@RequestBody TransactionRequest request, HttpServletResponse responses) throws IOException {

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
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_PDF);
            responseHeaders.setContentLength(reportArrayOutputStream.length);
            responseHeaders.setContentDispositionFormData("attachment", "receipt.pdf");
            return new ResponseEntity<>(reportArrayOutputStream, responseHeaders, HttpStatus.CREATED);

        }
        else{
            return response;
        }
    }

    @PostMapping("/assistant-make-transaction")
    public ResponseEntity<?> assistantMakeTransaction(@RequestBody TransactionRequest request, HttpServletResponse responses) throws IOException, JRException, SQLException {
        String email = AuthUtils.getCurrentlyLoggedInPerson();
        Owner assistant = assistantService.findByEmail(email).getOwner();

        EtimsTransactionDto etimsTransactionDto = generateEtimsRequest(request, assistant);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(etimsTransactionDto);

        ResponseEntity<?> response = transactionMiddleware.makeTransaction(jsonString);
        logger.info(response.toString());

        if (response.getStatusCode().value() == HttpStatus.CREATED.value()) {
            System.out.println(response);
            EtimsResponses.TransactionResponse transactionResponse = EtimsResponses.parseMakeTransactionResponse(Objects.requireNonNull(response.getBody()).toString());
            byte[] reportArrayOutputStream = jasperPDFService.assistantGenerateReport(transactionResponse.getData(), request).toByteArray();

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_PDF);
            header.setContentDispositionFormData("attachment", "Assistant-receipt.pdf");

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_PDF);
            responseHeaders.setContentLength(reportArrayOutputStream.length);
            responseHeaders.setContentDispositionFormData("attachment", "Assistant-receipt.pdf");

            return new ResponseEntity<>(reportArrayOutputStream, responseHeaders, HttpStatus.CREATED);

        } else {
            return response;
        }
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