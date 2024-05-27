package com.emtech.ushurusmart.transactions.service;

import com.emtech.ushurusmart.transactions.Dto.EtimsResponses;
import com.emtech.ushurusmart.transactions.Dto.EtimsResponses.TransactionResponse.EtimsData;
import com.emtech.ushurusmart.transactions.Dto.TransactionProduct;
import com.emtech.ushurusmart.transactions.Dto.TransactionRequest;
import com.emtech.ushurusmart.transactions.entity.Product;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.service.AssistantService;
import com.emtech.ushurusmart.usermanagement.utils.AuthUtils;

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
import java.sql.SQLException;

@Service
public class JasperPDFService {
    private static final String REPORT_TEMPLATE = "ETR-Reciept.jrxml";
    private static final String ASSISTANT_REPORT_TEMPLATE = "Assistant-ETR-Receipt.jrxml";
    private static final String CURRENCY = "KSHS";
    private static int COUNTER = 1; // renamed to follow Java naming conventions

    @Autowired
    private ProductService productService;

    @Autowired
    private AssistantService assistantService;

    public ByteArrayOutputStream exportJasperReport(EtimsResponses.TransactionResponse.TransactionData transactionData,
            TransactionRequest request) throws JRException, IOException, SQLException {
        Map<String, Object> parameters = new HashMap<>();
        
        List<Map<String, Object>> saleDataList = new ArrayList<>();
        List<EtimsResponses.TransactionResponse.Sale> etimsSales = transactionData.getSales();
        parameters.put("buyerKRAPin", transactionData.getBuyerPin());
        parameters.put("businessPin", transactionData.getEtims().getBusinessKRAPin());
        parameters.put("ownerPin", transactionData.getEtims().getBusinessOwnerKRAPin());
        parameters.put("etimsNumber", transactionData.getEtims().getEtimsCode());
        parameters.put("invoiceNumber", transactionData.getInvoiceNumber());
        parameters.put("amount", request.getSalesAmount());
        parameters.put("currency", CURRENCY);
        parameters.put("amount", request.getSalesAmount());
        for (EtimsResponses.TransactionResponse.Sale sale : etimsSales) {
            Map<String, Object> saleData = new HashMap<>();
            Product product = productService.findByName(sale.getName());
            EtimsResponses.TransactionResponse.Sale etimsSale = findSaleByName(etimsSales, product.getName());
            saleData.put("unitPrice", product.getUnitPrice());
            saleData.put("tax", etimsSale.getTax());
            saleData.put("counter", COUNTER);
            saleData.put("unitOfMeasure", product.getUnitOfMeasure());
            saleData.put("name", etimsSale.getName());
            saleData.put("taxable", etimsSale.isTaxable() ? "Taxable" : "Tax Exempted");            
            saleDataList.add(saleData);
            COUNTER++;
            System.out.println("this is the sales Data: " + saleDataList);
        }
        


        File reportTemplate = ResourceUtils.getFile("classpath:" + REPORT_TEMPLATE);
        JasperReport jasperReport = JasperCompileManager.compileReport(reportTemplate.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(saleDataList);
        System.out.println("this is the datasource output: " + dataSource);
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

    public static EtimsResponses.TransactionResponse.Sale findSaleByName(
            List<EtimsResponses.TransactionResponse.Sale> sales, String name) {
        return sales.stream()
                .filter(element -> element.getName().equals(name))
                .findFirst().orElse(null);
    }

    public ByteArrayOutputStream assistantGenerateReport(
            EtimsResponses.TransactionResponse.TransactionData transactionAssistantData,
            TransactionRequest assistantRequest) throws JRException, IOException, SQLException {
        Map<String, Object> parameters = new HashMap<>();

        List<EtimsResponses.TransactionResponse.Sale> etimsSales = transactionAssistantData.getSales();

        for (TransactionProduct assistantSale : assistantRequest.getSales()) {
            Product assistantProduct = productService.findById(assistantSale.getProductId());
            EtimsResponses.TransactionResponse.Sale etimsAssistantSale = findSaleByName(etimsSales,
                    assistantProduct.getName());
            EtimsResponses.TransactionResponse.EtimsData assistantEtimsData = new EtimsData();

            String email = AuthUtils.getCurrentlyLoggedInPerson();
            Assistant assistant = assistantService.findByEmail(email)
;

            parameters.put("buyerKRAPin", transactionAssistantData.getBuyerPin());
            parameters.put("etimsNumber", transactionAssistantData.getEtims().getBusinessOwnerKRAPin());
            // parameters.put("businessPin",
            // transactionAssistantData.getEtims().getBusinessKRAPin());
            parameters.put("invoiceNumber", transactionAssistantData.getEtims().getEtimsCode());
            parameters.put("counter", COUNTER);
            parameters.put("tax", etimsAssistantSale.getTax());
            parameters.put("currency", CURRENCY);
            parameters.put("taxable", etimsAssistantSale.isTaxable() ? "Taxable" : "Tax Exemptet");
            parameters.put("amount", assistantRequest.getSalesAmount());
            parameters.put("quantity", assistantSale.getQuantity());
            parameters.put("unitOfMeasure", assistantProduct.getUnitOfMeasure());
            parameters.put("unitPrice", assistantProduct.getUnitPrice());
            parameters.put("name", etimsAssistantSale.getName());
            parameters.put("branch", assistant);
            parameters.put("businessPin", assistantEtimsData.getBusinessKRAPin());
            parameters.put("assistantName", assistant.getBranch());
            parameters.put("ownerPin", transactionAssistantData.getEtims().getBusinessOwnerKRAPin());
        }

        File reportFile = ResourceUtils.getFile("classpath:" + ASSISTANT_REPORT_TEMPLATE);
        JasperReport jasperReport = JasperCompileManager.compileReport(reportFile.getAbsolutePath());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        configuration.setMetadataAuthor("Assistant");
        configuration.setMetadataCreator("Assistant");
        configuration.setCompressed(true);
        exporter.setConfiguration(configuration);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
        exporter.exportReport();
        return byteArrayOutputStream;
    }

}