package com.emtech.ushurusmart.transactions.service;

import com.emtech.ushurusmart.transactions.controller.TaxController;
import com.emtech.ushurusmart.transactions.entity.Product;
import jakarta.servlet.http.HttpServletResponse;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperPDFService {
    @Autowired
    private ProductService productService;

    public ByteArrayOutputStream exportJasperReport(HttpServletResponse response, String buyerPin, List<ProductInfo> products, TaxController.TransactionData product) throws JRException, IOException {
        List<Map<String, Object>> dataList = new ArrayList<>();
        String currency = "Kshs ";
        int counter = 1;
        for (ProductInfo info : products) {
            Product prod = productService.getById(info.getProductId());
            Map<String, Object> data = new HashMap<>();
            data.put("counter", counter);
            data.put("name", prod.getName());
            data.put("taxable", prod.isTaxable() ? "Taxable" : "Tax Exempted");
            data.put("unitPrice", currency + prod.getUnitPrice());
            data.put("quantity", info.getQuantity());
            data.put("unitOfMeasure", prod.getUnitOfMeasure());
            data.put("tax",23);
            data.put("amount", info.getAmount());
            dataList.add(data);
            counter++;

        }
        System.out.println(dataList);

        File file = ResourceUtils.getFile("C:\\Users\\PC\\Desktop\\backend\\src\\main\\resources\\ETR-Reciept.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);

        ProductInfo info;
        info = products.get(0);
        Product prod = productService.getById(info.getProductId());


        Map<String, Object> parameters = new HashMap<>();
        parameters.put("buyerKRAPin", info.getBuyerKRAPin());
        parameters.put("amount", info.getAmount());
        parameters.put("ownerPin", product.getOwnerPin());
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

    @Data
    @AllArgsConstructor
    public static class ProductInfo {
        private long productId;
        private int quantity;
        private String buyerKRAPin;
        private double amount;

    }


}
