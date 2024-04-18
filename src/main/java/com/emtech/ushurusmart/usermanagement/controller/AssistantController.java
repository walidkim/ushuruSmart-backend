package com.emtech.ushurusmart.usermanagement.controller;

import com.emtech.ushurusmart.Etims.Dtos.controller.TransactionDto;
import com.emtech.ushurusmart.Etims.controller.EtimTransactionController;
import com.emtech.ushurusmart.transactions.Dto.TransactionRequest;
import com.emtech.ushurusmart.transactions.entity.Product;
import com.emtech.ushurusmart.transactions.factory.EntityFactory;
import com.emtech.ushurusmart.transactions.service.JasperAssistantPDFService;
import com.emtech.ushurusmart.transactions.service.ProductService;
import com.emtech.ushurusmart.usermanagement.Dtos.entity.ProductDto;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.service.AssistantService;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.usermanagement.utils.AuthUtils;
import com.emtech.ushurusmart.utils.controller.ResContructor;
import com.emtech.ushurusmart.utils.controller.Responses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/assistant")
public class AssistantController {
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private AssistantService assistantService;
    @Autowired
    private EtimTransactionController etimTransactionController;
    @Autowired
    private JasperAssistantPDFService jasper;

    @Autowired
    private ProductService productService;
    @PostMapping("/make-transaction")
    public ResponseEntity<?> makeTransaction(@RequestBody TransactionRequest data, HttpServletResponse responses) throws IOException, JRException {
        TransactionDto etimAssistantReq = new TransactionDto();
        String assistantEmail = AuthUtils.getCurrentlyLoggedInPerson();
        Assistant assistant= assistantService.findByEmail(assistantEmail);
        Owner owner= assistant.getOwner();

        Product product = productService.findById(data.getProductId());
        etimAssistantReq.setTaxable(product.isTaxable());
        etimAssistantReq.setBussinessPin(owner.getBusinessKRAPin());
        etimAssistantReq.setBuyerPin(data.getBuyerKRAPin());
        etimAssistantReq.setName(assistant.getName());

        ResponseEntity<ResContructor> response = etimTransactionController.makeTransaction(etimAssistantReq);
        System.out.println("Fetched: " + response);

        if(response.getStatusCode()== HttpStatus.NOT_FOUND){
            return response;
        }

        TransactionData parsed = new TransactionData(Objects.requireNonNull(response.getBody()).getData().toString());
        List<JasperAssistantPDFService.ProductInfo> products = new ArrayList<>();
        products.add(new JasperAssistantPDFService.ProductInfo(data.getProductId(), data.getQuantity(), data.getBuyerKRAPin()));


        ByteArrayOutputStream report = jasper.exportAssistantJasperReport(responses, data.getBuyerKRAPin(), products, parsed);
        System.out.println("Generated Output Stream");
        byte[] reportBytes = report.toByteArray();
        report.close();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_PDF);
        responseHeaders.setContentLength(reportBytes.length);
        responseHeaders.setContentDispositionFormData("attachment","receipt.pdf");

        return new ResponseEntity<>(reportBytes,responseHeaders,HttpStatus.CREATED);

    }

    @Data
    public static class TransactionData {
        private long id;
        private double amount;
        private double tax;
        private String buyerPin;
        private boolean taxable;
        private String invoiceNumber;
        private String branch;
        private String etimsNumber;
        private String name;
        private String ownerPin;

        public TransactionData(String data) {
            String trimmedData = data.substring(12, data.length() - 1);
            String[] fields = trimmedData.split(",");
            for ( String field : fields ) {
                String[] keyValue = field.trim().split("=");
                switch(keyValue[0]){
                    case "id":
                        this.id = Long.parseLong(keyValue[1]);
                        break;
                    case "amount":
                        this.amount = Double.parseDouble(keyValue[1]);
                        break;
                    case "tax":
                        this.tax = Double.parseDouble(keyValue[1]);
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
                    case "branch":
                        this.branch = keyValue[1];
                    case "name":
                        this.name = keyValue[1];
                    case "ownerPin":
                        this.ownerPin = keyValue[1];
                    default:
                        System.out.println("Ignoring unknown field: "+ field);
                }
            }
        }

    }
    @GetMapping("/allProducts")
    public ResponseEntity<ResContructor> getAllProduct(){
        try {
            ResContructor res= new ResContructor();
            long id= ownerService.findByEmail(AuthUtils.getCurrentlyLoggedInPerson()).getId();
            res.setData(productService.findAllByOwnerId(id));
            res.setMessage("Products fetched successfully.");
            return  ResponseEntity.status(HttpStatus.OK).body(res);
        }catch (Exception e){
            return Responses.create500Response(e);
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id){
        ResContructor res= new ResContructor();
        try {
            Product product = productService.getById(id);

            if (product == null) {
                res.setMessage("No product with that Id exists");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
            }
            long ownerId= ownerService.findByEmail(AuthUtils.getCurrentlyLoggedInPerson()).getId();
            if(ownerId != product.getOwner().getId()){
                res.setMessage("You are not allowed to view this product.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
            }
            res.setMessage("Product fetched successfully.");
            res.setData(product);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch (Exception e){
            return Responses.create500Response(e);
        }

    }
    @GetMapping("/product/{name}")
    public ResponseEntity<Product> getProductByName(@RequestParam(name = "name", required = true) String name){
        Product product = productService.getByName(name);
        if (product == null) {
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }
    @PostMapping("/product/create")
    public ResponseEntity<ResContructor> addProduct(@RequestBody ProductDto prod){
        ResContructor res= new ResContructor();
        try {
            Owner owner= ownerService.findByEmail(AuthUtils.getCurrentlyLoggedInPerson());
            Product product= EntityFactory.createProduct(prod);
            product.setOwner(owner);
            res.setMessage("Product created successfully!");
            res.setData(productService.save(product));
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }catch (Exception e){
            return Responses.create500Response(e);
        }
    }
    @PutMapping("/product/update/{id}")
    public ResponseEntity<ResContructor> updateProduct(@PathVariable long id, @RequestBody ProductDto data){
        ResContructor res= new ResContructor();
        try {
            Product currentProduct = productService.getById(id);
            Owner owner= ownerService.findByEmail(AuthUtils.getCurrentlyLoggedInPerson());
            if (currentProduct == null){
                res.setMessage("No product with that id exists.");
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
            }
            if(owner.getId() != currentProduct.getOwner().getId()){
                res.setMessage("You are not authorized to modify this product.");
                return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
            }
            currentProduct= productService.save(EntityFactory.updateProduct(currentProduct,data));
            res.setMessage("Product updated successfully.");
            res.setData(currentProduct);
            return  ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e){
            return Responses.create500Response(e);
        }
    }
    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        try {
            ResContructor res= new ResContructor();
            Owner owner= ownerService.findByEmail(AuthUtils.getCurrentlyLoggedInPerson());
            if(productService.getById(id).getOwner().getId() != owner.getId()){
                res.setMessage("You are not authorized to delete this product.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
            }

            productService.delete(id);
            res.setMessage("Product deleted successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch ( Exception e){
            return Responses.create500Response(e);
        }
    }
}
