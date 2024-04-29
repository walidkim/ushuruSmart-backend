package com.emtech.ushurusmart.transactions.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JasperAssistantPDFService {

    @Autowired
    private ProductService productService;

    @Data
    @AllArgsConstructor
    public static class ProductInfo{
        private long ProductId;
        private int quantity;
        private String buyerKRAPin;
    }
//    public ByteArrayOutputStream exportAssistantJasperReport(HttpServletResponse response, String buyerPin, List<ProductInfo> products, AssistantController.TransactionData product) throws IOException, JRException {
//        int counter = 1;
//        String currency = "KSHs.";
//
//        List<Map<String, Object>> dataList = new ArrayList<>();
//        for (ProductInfo info : products) {
//            Product prod = productService.getById(info.getProductId());
//            Map<String, Object> data = new HashMap<>();
//            data.put("counter", counter);
//            data.put("name", prod.getName());
//            data.put("taxable", prod.isTaxable() ? "Taxable" : "Tax Exempted");
//            data.put("unitPrice", currency + prod.getUnitPrice());
//            data.put("quantity", info.getQuantity());
//            data.put("unitOfMeasure", prod.getUnitOfMeasure());
//            data.put("tax", currency + product.getTax());
//            dataList.add(data);
//            counter++;
//        }
//        File file = ResourceUtils.getFile("C:\\Users\\PC\\Desktop\\ushuru-smart-backend\\src\\main\\resources\\Assistant-ETR-Reciept.jrxml");
//        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
//        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);
//
//        ProductInfo info = products.get(0);
//        Product prod = productService.getById(info.getProductId());
//
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("buyerKRAPin", info.getBuyerKRAPin());
//        parameters.put("etimsNumber", product.getEtimsNumber());
//        parameters.put("branch", product.getBranch());
//        parameters.put("invoiceNumber", product.getInvoiceNumber());
//        parameters.put("invoiceDataset", dataSource);
//        parameters.put("name", prod.getName());
//        parameters.put("unitOfMeasure", prod.getUnitOfMeasure());
//        parameters.put("quantity", info.getQuantity());
//        parameters.put("unitPrice", prod.getUnitPrice());
//        parameters.put("currency", "KSHs.");
//        parameters.put("counter", 1);
//        parameters.put("tax", product.getTax());
//        parameters.put("assistantName", product.getName());
//        parameters.put("ownerPin", product.getOwnerPin());
//
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        JRPdfExporter exporter = new JRPdfExporter();
//        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
//        configuration.setCompressed(true);
//        exporter.setConfiguration(configuration);
//        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
//        exporter.exportReport();
//
//        return byteArrayOutputStream;
//
//    }
}
