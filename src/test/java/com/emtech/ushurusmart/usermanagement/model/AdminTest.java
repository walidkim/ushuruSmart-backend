package com.emtech.ushurusmart.usermanagement.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import com.emtech.ushurusmart.payment.model.PaymentDetails;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminTest {

    @Test
    public void testCreateAdmin() {
        Admin admin = new Admin();
        assertNotNull(admin);
    }

    @Test
    public void testSetAndGetId() {
        Admin admin = new Admin();
        admin.setId(1L);
        assertEquals(1L, admin.getId());
    }

    @Test
    public void testSetAndGetKRAPin() {
        Admin admin = new Admin();
        admin.setKRAPin("123456789");
        assertEquals("123456789", admin.getKRAPin());
    }

    @Test
    public void testSetAndGetUsername() {
        Admin admin = new Admin();
        admin.setEmail("admin");
        assertEquals("admin", admin.getEmail());
    }

    @Test
    public void testSetAndGetPassword() {
        Admin admin = new Admin();
        admin.setPassword("password");
        assertEquals("password", admin.getPassword());
    }

    @Test
    public void testSetAndGetUsersList() {
        Admin admin = new Admin();
        List<User> usersList = new ArrayList<>();
        usersList.add(new User());
        admin.setUsersList(usersList);
        assertEquals(usersList, admin.getUsersList());
    }

    @Test
    public void testSetAndGetPayments() {
        Admin admin = new Admin();
        List<PaymentDetails> payments = new ArrayList<>();
        payments.add(new PaymentDetails());
        admin.setPayments(payments);
        assertEquals(payments, admin.getPayments());
    }
}
