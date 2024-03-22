package com.emtech.ushurusmart.transactions.service;

import com.emtech.ushurusmart.transactions.Dto.TransactionRequest;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class InvoiceService {

    public byte[] generateInvoice(String buyerPin, List<TransactionRequest> products) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);
        int counter=1;

        PdfFont font = PdfFontFactory.createFont();

        // Adding buyer name
        Paragraph buyerParagraph = new Paragraph("Buyer: " + buyerPin).setFont(font);
        document.add(buyerParagraph);

        Paragraph columns = new Paragraph("No. Taxable Status \t Price: ").setFont(font);
        document.add(columns);

        // Adding product details
        for (TransactionRequest product : products) {
            Paragraph productParagraph = new Paragraph(counter + ")\t" + product.getType() + ",\t\t" + product.getPrice()).setFont(font);
            counter++;
            document.add(productParagraph);
        }

        // Calculating total
        double total = products.stream().mapToDouble(TransactionRequest::getPrice).sum();
        Paragraph totalParagraph = new Paragraph("Total: " + total).setFont(font);
        document.add(totalParagraph);
        document.close();

        return outputStream.toByteArray();
    }
}
