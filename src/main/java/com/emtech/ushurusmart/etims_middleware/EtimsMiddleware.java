package com.emtech.ushurusmart.etims_middleware;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class EtimsMiddleware {
    @Value("${server.port}")
    private String serverPort;
    public ResponseEntity<?> verifyBusinessKRAPin(String data){

        WebClient webClient = WebClient.create("http://localhost:" + serverPort);

        ClientResponse clientResponse = webClient.post()
                .uri("/api/v1/etims/verify")
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
}
