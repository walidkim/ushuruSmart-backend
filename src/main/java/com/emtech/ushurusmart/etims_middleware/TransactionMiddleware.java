package com.emtech.ushurusmart.etims_middleware;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class TransactionMiddleware {
    @Value("${server.port}")
     private String serverPort;
    public  ResponseEntity<?> makeTransaction(){


        WebClient webClient = WebClient.create("http://localhost:" + serverPort);

        // Assuming you want to send a JSON body
        String jsonBody = "{\"key\":\"value\"}"; // Replace this with your actual JSON body

        // Make the POST request and get the ClientResponse
        ClientResponse clientResponse = webClient.post()
                .uri("/api/v1/admin/add")
                .bodyValue(jsonBody)
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
