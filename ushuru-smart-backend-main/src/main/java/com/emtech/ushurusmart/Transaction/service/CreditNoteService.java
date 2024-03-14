package com.emtech.ushurusmart.Transaction.service;

import com.emtech.ushurusmart.Transaction.entity.CreditNote;
import com.emtech.ushurusmart.Transaction.entity.Products;
import com.emtech.ushurusmart.Transaction.entity.Transaction;
import com.emtech.ushurusmart.Transaction.repository.CreditNoteRepository;
import com.emtech.ushurusmart.Transaction.repository.ProductsRepository;
import com.emtech.ushurusmart.Transaction.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditNoteService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private CreditNoteRepository creditNoteRepository;

    @Transactional
    public CreditNote createCreditNoteFromSaleTransaction(String invoiceNumber) {
        Transaction transaction = transactionRepository.findByInvoiceNumber(invoiceNumber);
        if (transaction == null) {
            throw new RuntimeException("Sale transaction not found");
        }

        CreditNote creditNote = new CreditNote();
        creditNote.setInvoiceNumber(transaction.getInvoiceNumber());
        creditNote.setBuyerPin(transaction.getBuyerPin());
        creditNote.setProductId(transaction.getProductId());
        creditNote.setProductAmount(transaction.getProductUnitPrice());
        creditNote.setProductName(transaction.getProductName());
        creditNote.setProductQuantity(transaction.getProductQuantity());

        return creditNoteRepository.save(creditNote);
    }
    @Transactional
    public void approveCreditNoteAndRestockProducts(Long creditNoteId) {
        CreditNote creditNote = creditNoteRepository.findById(creditNoteId).orElseThrow(() -> new RuntimeException("Credit Note not found"));
        Products product = productsRepository.findByProductId(creditNote.getProductId());
        if (product != null) {
            product.setQuantity(product.getQuantity() + creditNote.getProductQuantity());
            productsRepository.save(product);
        }
    }
}
