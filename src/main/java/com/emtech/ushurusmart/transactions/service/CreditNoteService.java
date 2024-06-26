//package com.emtech.ushurusmart.transactions.service;
//
//import com.emtech.ushurusmart.transactions.entity.CreditNote;
//
//import com.emtech.ushurusmart.Etims.entity.Transaction;
//import com.emtech.ushurusmart.transactions.repository.CreditNoteRepository;
//import com.emtech.ushurusmart.transactions.repository.ProductRepository;
//import com.emtech.ushurusmart.Etims.repository.TransactionRepository;
//
//
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CreditNoteService {
//    @Autowired
//    private TransactionRepository transactionRepository;
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private CreditNoteRepository creditNoteRepository;
//
//    @Transactional
//    public CreditNote createCreditNoteFromSaleTransaction(String invoiceNumber) {
//        Transaction transaction = transactionRepository.findByInvoiceNumber(invoiceNumber);
//        if (transaction == null) {
//            throw new RuntimeException("Sale transaction not found");
//        }
//
//        CreditNote creditNote = new CreditNote();
//        creditNote.setInvoiceNumber(transaction.getInvoiceNumber());
//        creditNote.setBuyerPin(transaction.getBuyerPin());
//        creditNote.setProducts(transaction.getProducts());
//        creditNote.setProductAmount(transaction.getProductUnitPrice());
//        creditNote.setProductName(transaction.getProductName());
//        creditNote.setProductQuantity(transaction.getProductQuantity());
//
//        return creditNoteRepository.save(creditNote);
//    }
//
////    @Transactional
////    public void approveCreditNoteAndRestockProducts(Long creditNoteId) {
////        CreditNote creditNote = creditNoteRepository.findById(creditNoteId)
////                .orElseThrow(() -> new RuntimeException("Credit Note not found"));
////         product = productRepository.findById(creditNote.getProductId()).orElseGet(null);
////        if (product != null) {
////            product.setQuantity(product.getQuantity() + creditNote.getProductQuantity());
////            productRepository.save(product);
////        }
////    }
//}
