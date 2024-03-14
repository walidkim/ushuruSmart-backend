package com.emtech.ushurusmart.Transaction.controller;

import com.emtech.ushurusmart.Transaction.entity.CreditNote;
import com.emtech.ushurusmart.Transaction.service.CreditNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CreditNoteController {
    @Autowired
    private CreditNoteService creditNoteService;

    @PostMapping("/createCreditNote")
    public CreditNote createCreditNote(@RequestParam String invoiceNumber) {
        return creditNoteService.createCreditNoteFromSaleTransaction(invoiceNumber);
    }

    @PostMapping("/approveCreditNote")
    public void approveCreditNote(@RequestParam Long creditNoteId) {
        creditNoteService.approveCreditNoteAndRestockProducts(creditNoteId);
    }
}
