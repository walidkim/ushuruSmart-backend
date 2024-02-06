package com.emtech.ushurusmart.usermanagement.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.RollbackException;

public class UsersTest {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeEach
    public void setup() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void teardown() {
        if (entityManager != null) {
            entityManager.close();
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testSaveUser() {
        // Create a new user
        Users user = new Users();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setVerified(true);

        // Begin a transaction
        entityManager.getTransaction().begin();

        // Persist the user
        entityManager.persist(user);

        // Commit the transaction
        entityManager.getTransaction().commit();

        // Check if user is persisted with an ID
        assertNotNull(user.getUser_id());
    }

    @Test
    public void testInvalidUser() {
        // Create a new user with missing required fields
        Users user = new Users();

        // Begin a transaction
        entityManager.getTransaction().begin();

        // Try to persist the user without required fields
        assertThrows(RollbackException.class, () -> entityManager.persist(user));

        // Rollback the transaction
        entityManager.getTransaction().rollback();
    }
}
