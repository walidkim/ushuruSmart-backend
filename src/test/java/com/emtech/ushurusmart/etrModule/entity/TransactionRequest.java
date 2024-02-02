package com.emtech.ushurusmart.etrModule.entity;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntityResponseTest {

    @Test
    void testNoArgsConstructor() {
        EntityResponse<String> response = new EntityResponse<>();
        assertNotNull(response);
    }

    @Test
    void testAllArgsConstructor() {
        EntityResponse<String> response = new EntityResponse<>(200, "Success", "Body");
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("Success", response.getMessage());
        assertEquals("Body", response.getBody());
    }

    @Test
    void testGettersAndSetters() {
        EntityResponse<String> response = new EntityResponse<>();
        response.setStatusCode(404);
        response.setMessage("Not Found");
        response.setBody("Not Found Body");

        assertEquals(404, response.getStatusCode());
        assertEquals("Not Found", response.getMessage());
        assertEquals("Not Found Body", response.getBody());
    }

    @Test
    void testToString() {
        EntityResponse<String> response = new EntityResponse<>(500, "Internal Server Error", "Error Body");
        String expectedToString = "EntityResponse(statusCode=500, message=Internal Server Error, body=Error Body)";
        assertEquals(expectedToString, response.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        EntityResponse<String> response1 = new EntityResponse<>(200, "OK", "Success Body");
        EntityResponse<String> response2 = new EntityResponse<>(200, "OK", "Success Body");
        EntityResponse<String> response3 = new EntityResponse<>(404, "Not Found", "Not Found Body");

        // Test equality
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);

        // Test hash code
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }
}
