package com.emtech.ushurusmart.etrModule.controller;

import com.emtech.ushurusmart.etrModule.entity.TransactionRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void makeTransaction_Success() throws Exception {
        // Given
        TransactionRequest request = new TransactionRequest("vatible", 100.0);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/api/make-transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"" + request.getType() + "\",\"price\":" + request.getPrice() + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Transaction successful"))
                .andExpect(jsonPath("$.body").value("Transaction of type 'vatible' with price 100.0 processed."));
    }

    @Test
    void makeTransaction_UnknownProductType() throws Exception {
        // Given
        TransactionRequest request = new TransactionRequest("unknown", 100.0);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/api/make-transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"" + request.getType() + "\",\"price\":" + request.getPrice() + "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Unknown product type: unknown"));
    }

    @Test
    void makeTransaction_MissingType() throws Exception {
        // Given
        TransactionRequest request = new TransactionRequest(null, 100.0);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/api/make-transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"price\":" + request.getPrice() + "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Type is required"));
    }

    @Test
    void makeTransaction_MissingPrice() throws Exception {
        // Given
        TransactionRequest request = new TransactionRequest("vatible", (Double) null);

        // Whenß
        mockMvc.perform(MockMvcRequestBuilders.post("/api/make-transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"" + request.getType() + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Price is required"));
    }
}
