package com.emtech.ushurusmart.transactions.controller;


import com.emtech.ushurusmart.etims_middleware.TransactionMiddleware;
import com.emtech.ushurusmart.transactions.Dto.EtimsProduct;
import com.emtech.ushurusmart.transactions.Dto.EtimsTransactionDto;
import com.emtech.ushurusmart.transactions.Dto.TransactionProduct;
import com.emtech.ushurusmart.transactions.Dto.TransactionRequest;
import com.emtech.ushurusmart.transactions.entity.Product;
import com.emtech.ushurusmart.transactions.service.JasperPDFService;
import com.emtech.ushurusmart.transactions.service.ProductService;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.usermanagement.utils.AuthUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/tax")
public class TaxController {


    @Autowired
    private TransactionMiddleware transactionMiddleware;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private JasperPDFService jasperPDFService;


    @PostMapping("/make-transaction")
    public ResponseEntity<?> makeTransaction(@RequestBody TransactionRequest request, HttpServletResponse responses) throws IOException, JRException {
        System.out.println(" The data is " + request);

        Owner owner = ownerService.findByEmail(AuthUtils.getCurrentlyLoggedInPerson());

        EtimsTransactionDto etimReq = generateEtimsRequest(request, owner);


        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(etimReq);


        ResponseEntity<?> response = transactionMiddleware.makeTransaction(jsonString);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            TransactionResponse transactionResponse = objectMapper.readValue(response.getBody().toString(), TransactionResponse.class);


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice.pdf");
            System.out.println("Generated PDF");

            ByteArrayOutputStream reportArrayOutputStream = jasperPDFService.exportJasperReport(request, transactionResponse.getData());
            System.out.println("Generated output Stream");
            byte[] reportBytes = reportArrayOutputStream.toByteArray();
            reportArrayOutputStream.close();

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_PDF);
            responseHeaders.setContentLength(reportBytes.length);
            responseHeaders.setContentDispositionFormData("attachment", "receipt.pdf");

            return new ResponseEntity<>(reportBytes, responseHeaders, HttpStatus.CREATED);

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

        return ResponseEntity.ok("Good");

    }

    @NotNull
    private EtimsTransactionDto generateEtimsRequest(TransactionRequest request, Owner owner) {
        List<JasperPDFService.ProductInfo> reportProducts = new ArrayList<>();

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
            reportProducts.add(new JasperPDFService.ProductInfo(sold.getProductId(), sold.getQuantity(), request.getBuyerKRAPin(), amount));
            etimsSales.add(etimsProduct);
        }
        etimReq.setSales(etimsSales);
        return etimReq;
    }

    @Data
    public static class TransactionData {

        private long id;
        private double totalAmount;
        private double tax;
        private String ownerPin;
        private String buyerPin;
        private boolean taxable;
        private String invoiceNumber;
        private String etimsNumber;
        private String dateCreated;

        public TransactionData(String data) {
            String trimmedData = data.substring(12, data.length() - 1); // Remove "Transaction(" and ")"
            String[] fields = trimmedData.split(","); // Split by comma
            for (String field : fields) {
                String[] keyValue = field.trim().split("="); // Split by equal sign
                switch (keyValue[0]) {
                    case "id":
                        this.id = Long.parseLong(keyValue[1]);
                        break;
                    case "totalAmount":
                        this.totalAmount = Double.parseDouble(keyValue[1]);
                        break;
                    case "tax":
                        this.tax = Double.parseDouble(keyValue[1]);
                        break;
                    case "ownerPin":
                        this.ownerPin = keyValue[1];
                        break;
                    case "buyerPin":
                        this.buyerPin = keyValue[1];
                        break;
                    case "taxable":
                        this.taxable = Boolean.parseBoolean(keyValue[1]);
                        break;
                    case "invoiceNumber":
                        this.invoiceNumber = keyValue[1];
                        break;
                    case "etimsNumber":
                        this.etimsNumber = keyValue[1];
                        break;
                    case "dateCreated":
                        this.dateCreated = keyValue[1];
                        break;
                    default:
                        // Handle unknown fields (optional)
                        System.out.println("Ignoring unknown field: " + field);
                }
            }
        }
    }


    @Data
    public static class TransactionResponse {

        @JsonProperty("message")
        private String message;

        @JsonProperty("data")
        private TransactionData data;

        @Data
        public static class TransactionData {

            @JsonProperty("id")
            private int id;

            @JsonProperty("amount")
            private double amount;

            @JsonProperty("buyerPin")
            private String buyerPin;

            @JsonProperty("invoiceNumber")
            private String invoiceNumber;

            @JsonProperty("tax")
            private double tax;

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            @JsonProperty("dateCreated")
            private String dateCreated;

            @JsonProperty("sales")
            private List<Sale> sales;
            @JsonProperty("etims")
            private Object etims;
        }

        @Data
        public static class Sale {

            @JsonProperty("id")
            private int id;

            @JsonProperty("taxable")
            private boolean taxable;

            @JsonProperty("tax")
            private double tax;

            @JsonProperty("name")
            private String name;

            @JsonProperty("amount")
            private double amount;
        }
    }

}