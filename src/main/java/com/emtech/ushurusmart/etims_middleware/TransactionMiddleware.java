package com.emtech.ushurusmart.etims_middleware;

import com.emtech.ushurusmart.utils.controller.ResContructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.Date;


@Component
public class TransactionMiddleware {
    @Value("${server.port}")
     private String serverPort;
    public  ResponseEntity<?> makeTransaction(String data){

        WebClient webClient = WebClient.create("http://localhost:" + serverPort);

        ClientResponse clientResponse = webClient.post()
                .uri("/api/v1/etims/tax/make-transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(data)
                .exchange()
                .block(); // Block to wait for the response

        if (clientResponse != null) {
            // Convert the ClientResponse to a ResponseEntity
            String body = clientResponse.bodyToMono(String.class).block();
            return ResponseEntity.status(clientResponse.statusCode())
                    .body(body);
        } else {
            // Handle the case where the response is null (e.g., due to a timeout)
            return ResponseEntity.status(500).body("Error: No response received");
        }
    }

    public  ResponseEntity<?> getRangeTransactions(LocalDate startDate, LocalDate endDate){

        WebClient webClient = WebClient.create("http://localhost:" + serverPort);

        ClientResponse clientResponse = webClient.get()
                .uri("/api/v1/etims/tax/make-range?startDate=%s&endDate=%s", startDate, endDate)
                .exchange()
                .block(); // Block to wait for the response

        if (clientResponse != null) {
            // Convert the ClientResponse to a ResponseEntity
            String body = clientResponse.bodyToMono(String.class).block();
            return ResponseEntity.status(clientResponse.statusCode())
                    .body(body);
        } else {
            // Handle the case where the response is null (e.g., due to a timeout)
            return ResponseEntity.status(500).body("Error: No response received");
        }
    }
}
