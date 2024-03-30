package com.emtech.ushurusmart.transactions.controller;

import com.emtech.ushurusmart.Etims.Dtos.controller.TransactionDto;
import com.emtech.ushurusmart.Etims.controller.EtimTransactionController;
import com.emtech.ushurusmart.transactions.Dto.TransactionRequest;
import com.emtech.ushurusmart.transactions.entity.Product;
import com.emtech.ushurusmart.transactions.service.InvoiceService;
import com.emtech.ushurusmart.transactions.service.ProductService;
import com.emtech.ushurusmart.transactions.service.TaxCalculator;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.usermanagement.utils.AuthUtils;
import com.emtech.ushurusmart.utils.controller.ResContructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tax")
public class TransactionController {
    @Autowired
    private TaxCalculator taxCalculatorService;

    @Autowired
    private OwnerService ownerService;


    @Autowired
    private ProductService productService;
    @Autowired
    private EtimTransactionController etimTransactionController;

    @Autowired
    private InvoiceService pdfService;

    @PostMapping("/make-transaction")
    public ResponseEntity<?> makeTransaction(@RequestBody TransactionRequest data) throws IOException {
        TransactionDto etimReq= new TransactionDto();
        Owner owner = ownerService.findByEmail(AuthUtils.getCurrentlyLoggedInPerson());
        Product product= productService.findById(data.getProductId());
        etimReq.setOwnerPin(owner.getKRAPin());
        etimReq.setTaxable(product.isTaxable());
        etimReq.setAmount(product.getUnitPrice() * data.getQuantity());
        etimReq.setBussinessPin(owner.getBusinessKRAPin());
        etimReq.setBuyerPin(data.getBuyerKRAPin());
        ResponseEntity<ResContructor> response = etimTransactionController.makeTransaction(etimReq);
        TransactionData parsed= new TransactionData(response.getBody().getData().toString());
        List<InvoiceService.ProductInfo> products= new ArrayList<>();
        products.add(new InvoiceService.ProductInfo(data.getProductId(),data.getQuantity()));


       byte[] invoice = pdfService.generateInvoice(data.getBuyerKRAPin(),products,parsed);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "invoice-test.pdf");

// Wrap the byte array in a ByteArrayResource
           ByteArrayResource resource = new ByteArrayResource(invoice);

// Return the ResponseEntity with the resource as the body
        return new ResponseEntity<>(resource,headers, HttpStatus.CREATED);

    }


    @Data
    public static class TransactionData {

        private long id;
        private double amount;
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
                    case "amount":
                        this.amount = Double.parseDouble(keyValue[1]);
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




}