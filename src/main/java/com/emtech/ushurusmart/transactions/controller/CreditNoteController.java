package com.emtech.ushurusmart.transactions.controller;

import com.emtech.ushurusmart.transactions.entity.CreditNote;
import com.emtech.ushurusmart.transactions.service.CreditNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/credit-notes")
public class CreditNoteController {
    @Autowired
    private CreditNoteService creditNoteService;

    @PostMapping("/create")
    public CreditNote createCreditNote(@RequestParam String invoiceNumber) {
        return creditNoteService.createCreditNoteFromSaleTransaction(invoiceNumber);
    }

    @PostMapping("/approve")
    public void approveCreditNote(@RequestParam Long creditNoteId) {
        creditNoteService.approveCreditNoteAndRestockProducts(creditNoteId);
    }
}
