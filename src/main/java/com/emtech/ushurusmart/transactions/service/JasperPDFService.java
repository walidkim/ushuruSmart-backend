package com.emtech.ushurusmart.transactions.service;

import com.emtech.ushurusmart.transactions.Dto.TransactionProduct;
import com.emtech.ushurusmart.transactions.Dto.TransactionRequest;
import com.emtech.ushurusmart.transactions.controller.TaxController;
import com.emtech.ushurusmart.transactions.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class JasperPDFService {
    @Autowired
    private ProductService productService;

    public ByteArrayOutputStream exportJasperReport(TransactionRequest request, TaxController.TransactionResponse.TransactionData etimsResponse) throws JRException, IOException {
        List<Map<String, Object>> dataList = new ArrayList<>();
        String currency = "Kshs ";
        int counter = 1;
        double totalAmount= 0.0;
        double totalTax= 0.0;

        List<TaxController.TransactionResponse.Sale> etimsSales= etimsResponse.getSales();
        for (TransactionProduct incomingSale : request.getSales()) {
            Product product= productService.getById(incomingSale.getProductId());
             TaxController.TransactionResponse.Sale etimsSale= findSaleByName(etimsSales, product.getName());

            if(etimsSale!= null){
                Map<String, Object> data = new HashMap<>();
                data.put("counter", counter);
                data.put("name", product.getName());
                data.put("taxable", product.isTaxable() ? "Taxable" : "Tax Exempted");
                data.put("unitPrice", currency + product.getUnitPrice());
                data.put("quantity", incomingSale.getQuantity());
                data.put("unitOfMeasure", product.getUnitOfMeasure());
                data.put("tax", etimsSale.getTax());
                data.put("amount", etimsSale.getAmount());
                dataList.add(data);
                totalAmount+= etimsSale.getAmount();
                totalTax+=etimsSale.getTax();
                counter++;
            }

        }
        System.out.println(dataList);

        File file = ResourceUtils.getFile("C:\\Users\\PC\\Desktop\\backend\\src\\main\\resources\\ETR-Reciept.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);



        Map<String, Object> parameters = new HashMap<>();
        parameters.put("buyerKRAPin", request.getBuyerKRAPin());
        parameters.put("amount",totalAmount);
        parameters.put("ownerPin", etimsResponse.get());
        parameters.put("etimsNumber", product.getEtimsNumber());
        parameters.put("invoiceNumber", product.getInvoiceNumber());
        parameters.put("invoiceDataset", dataSource);
        parameters.put("name", prod.getName());
        parameters.put("unitOfMeasure", prod.getUnitOfMeasure());
        parameters.put("quantity", info.getQuantity());
        parameters.put("unitPrice", prod.getUnitPrice());
        parameters.put("currency", "KSHs.");
        parameters.put("taxable", prod.isTaxable() ? "Taxable" : "Tax Exempted");
        parameters.put("counter", 1);
        parameters.put("tax", product.getTax());
        System.out.println(parameters);


        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        configuration.setCompressed(true);
        exporter.setConfiguration(configuration);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
        exporter.exportReport();
        return byteArrayOutputStream;
    }

    public static TaxController.TransactionResponse.Sale findSaleByName(List<TaxController.TransactionResponse.Sale> sales, String name) {
        return sales.stream()
                .filter(element -> element.getName().equals(name))
                .findFirst().orElse(null);
    }

    @Data
    @AllArgsConstructor
    public static class ProductInfo {
        private long productId;
        private int quantity;
        private String buyerKRAPin;
        private double amount;

    }


}
