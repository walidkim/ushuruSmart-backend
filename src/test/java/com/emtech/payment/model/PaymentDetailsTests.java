package com.emtech.payment.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.emtech.ushurusmart.payment.model.PaymentDetails;
import com.emtech.ushurusmart.usermanagement.model.Admin;
import org.junit.jupiter.api.Test;

public class PaymentDetailsTests {

    @Test
    public void testCreatePaymentDetails() {
        PaymentDetails paymentDetails = new PaymentDetails();
        assertNotNull(paymentDetails);
    }

    @Test
    public void testSetAndGetId() {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setId(1L);
        assertEquals(1L, paymentDetails.getId());
    }

    @Test
    public void testSetAndGetAmount() {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setAmount(100.50);
        assertEquals(100.50, paymentDetails.getAmount());
    }

    @Test
    public void testSetAndGetAdmin() {
        PaymentDetails paymentDetails = new PaymentDetails();
        Admin admin = mock(Admin.class);
        paymentDetails.setAdmin(admin);
        assertEquals(admin, paymentDetails.getAdmin());
    }

    @Test
    public void testSetAndGetMethod() {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setMethod("Credit Card");
        assertEquals("Credit Card", paymentDetails.getMethod());
    }

    @Test
    public void testSetAndGetPaymentCode() {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setPaymentCode("ABC123");
        assertEquals("ABC123", paymentDetails.getPaymentCode());
    }

    @Test
    public void testSetAndGetESlipNumber() {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setESlipNumber(123456789L);
        assertEquals(123456789L, paymentDetails.getESlipNumber());
    }

    @Test
    public void testSetAndGetIsPaid() {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setIsPaid(true);
        assertTrue(paymentDetails.getIsPaid());
    }
}
