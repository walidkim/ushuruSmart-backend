package com.emtech.ushurusmart.transactions.service;


import com.emtech.ushurusmart.transactions.Dto.EtimsResponses;
import com.emtech.ushurusmart.transactions.Dto.TransactionProduct;
import com.emtech.ushurusmart.transactions.Dto.TransactionRequest;
import com.emtech.ushurusmart.transactions.entity.Product;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class InvoiceService {

 @Autowired
 private ProductService productService;

    public byte[] generateInvoice(EtimsResponses.TransactionResponse.TransactionData transactionData, TransactionRequest request) throws IOException {

       String currency= "Kshs ";
       int counter=1;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        FontProgram boldFontProgram = FontProgramFactory.createFont("Helvetica-Bold");
        FontProgram normalFontProgram = FontProgramFactory.createFont("Helvetica");

// Create PdfFont objects
        PdfFont boldFont = PdfFontFactory.createFont(boldFontProgram, "UTF-8");
        PdfFont normalFont = PdfFontFactory.createFont(normalFontProgram, "UTF-8");
        // Create a table for product details
        Table productTable = new Table(new float[]{1, 2, 1, 1,1,2, 2});  // Define column widths

        // Header row with light gray background
        productTable.addHeaderCell(new Cell().setBackgroundColor(new DeviceRgb(220, 220, 220))
                .setFont(boldFont).add(new Paragraph("No)")).setTextAlignment(TextAlignment.CENTER));
        productTable.addHeaderCell(new Cell().setBackgroundColor(new DeviceRgb(220, 220, 220))
                .setFont(boldFont).add(new Paragraph("Name")).setTextAlignment(TextAlignment.CENTER));
        productTable.addHeaderCell(new Cell().setBackgroundColor(new DeviceRgb(220, 220, 220))
                .setFont(boldFont).add(new Paragraph("Taxable Status:")).setTextAlignment(TextAlignment.CENTER));
        productTable.addHeaderCell(new Cell().setBackgroundColor(new DeviceRgb(220, 220, 220))
                .setFont(boldFont).add(new Paragraph("Price:")).setTextAlignment(TextAlignment.CENTER));
        productTable.addHeaderCell(new Cell().setBackgroundColor(new DeviceRgb(220, 220, 220))
                .setFont(boldFont).add(new Paragraph("Quantity:")).setTextAlignment(TextAlignment.CENTER));
        productTable.addHeaderCell(new Cell().setBackgroundColor(new DeviceRgb(220, 220, 220))
                .setFont(boldFont).add(new Paragraph("Total Amount:")).setTextAlignment(TextAlignment.CENTER));

     productTable.addHeaderCell(new Cell().setBackgroundColor(new DeviceRgb(220, 220, 220))
             .setFont(boldFont).add(new Paragraph("Total Tax:")).setTextAlignment(TextAlignment.CENTER));

     List<EtimsResponses.TransactionResponse.Sale> etimsSales= transactionData.getSales();
        // Add product details as a table row
     for(TransactionProduct sale: request.getSales()){

      Product product= productService.findById(sale.getProductId());
      EtimsResponses.TransactionResponse.Sale etimsSale=findSaleByName(etimsSales,product.getName());

      productTable.addCell(new Cell().setFont(normalFont).add(new Paragraph(String.valueOf(counter))));
      productTable.addCell(new Cell().setFont(normalFont).add(new Paragraph(String.valueOf(etimsSale.getName()))));
      productTable.addCell(new Cell().setFont(normalFont).add(new Paragraph(String.valueOf(etimsSale.isTaxable()?"Taxable":"Tax Exempted"))));
      productTable.addCell(new Cell().setFont(normalFont).add(new Paragraph(currency + (product.getUnitPrice()))));
      productTable.addCell(new Cell().setFont(normalFont).add(new Paragraph( " "+(sale.getQuantity())+" "+product.getUnitOfMeasure())));
      productTable.addCell(new Cell().setFont(normalFont).add(new Paragraph(currency + (etimsSale.getAmount()))));
      productTable.addCell(new Cell().setFont(normalFont).add(new Paragraph(currency + (etimsSale.getTax()))));
      counter ++;
     }
        // Add table to document
       Paragraph paragraph=  new Paragraph("Buyer KRA PIN: ").setFont(boldFont);
        Paragraph buyer= new Paragraph(transactionData.getBuyerPin()).setFont(normalFont).setMarginBottom(10.0f);
       document.add(paragraph.add(buyer));
        document.add(productTable);

        // Owner details with some formatting
        Paragraph details = new Paragraph();
        details.add("Owner KRA Pin: ").setFont(boldFont).add(new Paragraph(transactionData.getEtims().getBusinessOwnerKRAPin()).setFont(normalFont));
        details.add("\n");
        details.add("Etims Code: ").setFont(boldFont).add(new Paragraph(transactionData.getEtims().getEtimsCode()).setFont(normalFont));
        details.add("\n");
        details.add("Invoice Number: ").setFont(boldFont).add(new Paragraph(transactionData.getInvoiceNumber()).setFont(normalFont));
        details.setMarginTop(10);
        details.add("\n");
        details.add("Total Tax: ").setFont(boldFont).add(new Paragraph(currency + (transactionData.getTax())).setFont(normalFont));
        details.add("\n");
        details.add("Total Amount: ").setFont(boldFont).add(new Paragraph(currency + (transactionData.getAmount())).setFont(normalFont));
        details.add("\n");

        details.add("Net Total: ").setFont(boldFont).add(new Paragraph(currency + (transactionData.getAmount() + transactionData.getTax())).setFont(normalFont));
        details.add("\n");

        document.add(details.setMarginTop(10));


        document.close();

        return outputStream.toByteArray();
    }


 public static EtimsResponses.TransactionResponse.Sale findSaleByName(List<EtimsResponses.TransactionResponse.Sale> sales, String name) {
  return sales.stream()
          .filter(element -> element.getName().equals(name))
          .findFirst().orElse(null);
 }


 @Data
 @AllArgsConstructor
 public static class ProductInfo{
  private long productId;
  private int  quantity;

 }
}
