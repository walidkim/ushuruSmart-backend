//
//package com.emtech.ushurusmart.usermanagement.controller;
//
//import com.emtech.ushurusmart.Etims.Dtos.controller.TransactionDto;
//import com.emtech.ushurusmart.Etims.controller.TransactionController;
//import com.emtech.ushurusmart.transactions.Dto.TransactionRequest;
//import com.emtech.ushurusmart.transactions.service.JasperAssistantPDFService;
//import com.emtech.ushurusmart.transactions.service.ProductService;
//import com.emtech.ushurusmart.usermanagement.model.Assistant;
//import com.emtech.ushurusmart.usermanagement.model.Owner;
//import com.emtech.ushurusmart.usermanagement.service.AssistantService;
//import com.emtech.ushurusmart.usermanagement.utils.AuthUtils;
//import com.emtech.ushurusmart.utils.controller.ResContructor;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.Data;
//import net.sf.jasperreports.engine.JRException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//@RestController
//@RequestMapping("/api/v1/assistant/tax")
//public class AssistantController {
//
//    @Autowired
//    private AssistantService assistantService;
//    @Autowired
//    private TransactionController etimTransactionController;
//    @Autowired
//    private JasperAssistantPDFService jasper;
//
//    @Autowired
//    private ProductService productService;
//    @PostMapping("make-transaction")
//    public ResponseEntity<?> makeTransaction(@RequestBody TransactionRequest data, HttpServletResponse responses) throws IOException, JRException {
//        TransactionDto etimAssistantReq = new TransactionDto();
//        String assistantEmail = AuthUtils.getCurrentlyLoggedInPerson();
//        Assistant assistant= assistantService.findByEmail(assistantEmail);
//        Owner owner= assistant.getOwner();
//
//        ResponseEntity<ResContructor> response = etimTransactionController.makeTransaction(null);
//        System.out.println("Fetched: " + response);
//
//        if(response.getStatusCode()== HttpStatus.NOT_FOUND){
//            return response;
//        }
//
//        TransactionData parsed = new TransactionData(Objects.requireNonNull(response.getBody()).getData().toString());
//        List<JasperAssistantPDFService.ProductInfo> products = new ArrayList<>();
//        products.add(new JasperAssistantPDFService.ProductInfo(data.getProductId(), data.getQuantity(), data.getBuyerKRAPin()));
//
//
//        ByteArrayOutputStream report = jasper.exportAssistantJasperReport(responses, data.getBuyerKRAPin(), products, parsed);
//        System.out.println("Generated Output Stream");
//        byte[] reportBytes = report.toByteArray();
//        report.close();
//
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.setContentType(MediaType.APPLICATION_PDF);
//        responseHeaders.setContentLength(reportBytes.length);
//        responseHeaders.setContentDispositionFormData("attachment","receipt.pdf");
//
//        return new ResponseEntity<>(reportBytes,responseHeaders,HttpStatus.CREATED);
//
//    }
//
//
//    @Data
//    public static class TransactionData {
//        private long id;
//        private double amount;
//        private double tax;
//        private String buyerPin;
//        private boolean taxable;
//        private String invoiceNumber;
//        private String branch;
//        private String etimsNumber;
//        private String name;
//        private String ownerPin;
//
//        public TransactionData(String data) {
//            String trimmedData = data.substring(12, data.length() - 1);
//            String[] fields = trimmedData.split(",");
//            for ( String field : fields ) {
//                String[] keyValue = field.trim().split("=");
//                switch(keyValue[0]){
//                    case "id":
//                        this.id = Long.parseLong(keyValue[1]);
//                        break;
//                    case "amount":
//                        this.amount = Double.parseDouble(keyValue[1]);
//                        break;
//                    case "tax":
//                        this.tax = Double.parseDouble(keyValue[1]);
//                        break;
//                    case "buyerPin":
//                        this.buyerPin = keyValue[1];
//                        break;
//                    case "taxable":
//                        this.taxable = Boolean.parseBoolean(keyValue[1]);
//                        break;
//                    case "invoiceNumber":
//                        this.invoiceNumber = keyValue[1];
//                        break;
//                    case "branch":
//                        this.branch = keyValue[1];
//                    case "name":
//                        this.name = keyValue[1];
//                    case "ownerPin":
//                        this.ownerPin = keyValue[1];
//                    default:
//                        System.out.println("Ignoring unknown field: "+ field);
//                }
//            }
//        }
//
//    }
//
//}
//

//package com.emtech.ushurusmart.usermanagement.controller;

//import com.emtech.ushurusmart.Etims.Dtos.controller.TransactionDto;
//import com.emtech.ushurusmart.Etims.controller.TransactionController;
//import com.emtech.ushurusmart.transactions.Dto.TransactionRequest;
//import com.emtech.ushurusmart.transactions.service.JasperAssistantPDFService;
//import com.emtech.ushurusmart.transactions.service.ProductService;
//import com.emtech.ushurusmart.usermanagement.model.Assistant;
//import com.emtech.ushurusmart.usermanagement.model.Owner;
//import com.emtech.ushurusmart.usermanagement.service.AssistantService;
//import com.emtech.ushurusmart.usermanagement.utils.AuthUtils;
//import com.emtech.ushurusmart.utils.controller.ResContructor;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.Data;
//import net.sf.jasperreports.engine.JRException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//@RestController
//@RequestMapping("/api/v1/assistant/tax")
//public class AssistantController {
//
//    @Autowired
//    private AssistantService assistantService;
//    @Autowired
//    private TransactionController etimTransactionController;
//    @Autowired
//    private JasperAssistantPDFService jasper;
//
//    @Autowired
//    private ProductService productService;
//    @PostMapping("make-transaction")
//    public ResponseEntity<?> makeTransaction(@RequestBody TransactionRequest data, HttpServletResponse responses) throws IOException, JRException {
//        TransactionDto etimAssistantReq = new TransactionDto();
//        String assistantEmail = AuthUtils.getCurrentlyLoggedInPerson();
//        Assistant assistant= assistantService.findByEmail(assistantEmail);
//        Owner owner= assistant.getOwner();
//
//        ResponseEntity<ResContructor> response = etimTransactionController.makeTransaction(null);
//        System.out.println("Fetched: " + response);
//
//        if(response.getStatusCode()== HttpStatus.NOT_FOUND){
//            return response;
//        }
//
//        TransactionData parsed = new TransactionData(Objects.requireNonNull(response.getBody()).getData().toString());
//        List<JasperAssistantPDFService.ProductInfo> products = new ArrayList<>();
//        products.add(new JasperAssistantPDFService.ProductInfo(data.getProductId(), data.getQuantity(), data.getBuyerKRAPin()));
//
//
//        ByteArrayOutputStream report = jasper.exportAssistantJasperReport(responses, data.getBuyerKRAPin(), products, parsed);
//        System.out.println("Generated Output Stream");
//        byte[] reportBytes = report.toByteArray();
//        report.close();
//
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.setContentType(MediaType.APPLICATION_PDF);
//        responseHeaders.setContentLength(reportBytes.length);
//        responseHeaders.setContentDispositionFormData("attachment","receipt.pdf");
//
//        return new ResponseEntity<>(reportBytes,responseHeaders,HttpStatus.CREATED);
//
//    }
//
//
//    @Data
//    public static class TransactionData {
//        private long id;
//        private double amount;
//        private double tax;
//        private String buyerPin;
//        private boolean taxable;
//        private String invoiceNumber;
//        private String branch;
//        private String etimsNumber;
//        private String name;
//        private String ownerPin;
//
//        public TransactionData(String data) {
//            String trimmedData = data.substring(12, data.length() - 1);
//            String[] fields = trimmedData.split(",");
//            for ( String field : fields ) {
//                String[] keyValue = field.trim().split("=");
//                switch(keyValue[0]){
//                    case "id":
//                        this.id = Long.parseLong(keyValue[1]);
//                        break;
//                    case "amount":
//                        this.amount = Double.parseDouble(keyValue[1]);
//                        break;
//                    case "tax":
//                        this.tax = Double.parseDouble(keyValue[1]);
//                        break;
//                    case "buyerPin":
//                        this.buyerPin = keyValue[1];
//                        break;
//                    case "taxable":
//                        this.taxable = Boolean.parseBoolean(keyValue[1]);
//                        break;
//                    case "invoiceNumber":
//                        this.invoiceNumber = keyValue[1];
//                        break;
//                    case "branch":
//                        this.branch = keyValue[1];
//                    case "name":
//                        this.name = keyValue[1];
//                    case "ownerPin":
//                        this.ownerPin = keyValue[1];
//                    default:
//                        System.out.println("Ignoring unknown field: "+ field);
//                }
//            }
//        }
//
//    }
//
//}
//
