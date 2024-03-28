package com.emtech.ushurusmart.usermanagement.controller.owner;

import com.emtech.ushurusmart.usermanagement.controller.Utils;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.model.Role;
import com.emtech.ushurusmart.usermanagement.repository.AssistantRepository;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ValidatableResponse;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;




@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "")
public class OwnerActionTest {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private AssistantRepository assistantRepository;


    @LocalServerPort
    private int port;

    private String token;

    private String signUpUrl;

    private String loginUrl;

    @BeforeEach
    public void setup() throws IOException {

        ownerRepository.deleteAll();

        signUpUrl = "http://localhost:" + port + "/api/v1/auth/sign-up?type=owner";
        loginUrl = "http://localhost:" + port + "/api/v1/auth/login?type=owner";
        loginAndGetToken();

    }

    @AfterEach
    public void tearDown() {
        ownerRepository.deleteAll();
    }

    private void loginAndGetToken() throws IOException {
        Owner owner = new Owner();
        if (ownerRepository.findAll().isEmpty()) {
            owner.setName("test");
            owner.setEmail("test@test.com");
            owner.setPassword("test");
            owner.setPhoneNumber("25489898989");
            owner.setRole(Role.owner);
            ownerService.save(owner);
        }
        String loginJson = "{\"email\":\"test@test.com\",\"password\":\"test\"}";
        ValidatableResponse res = given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(201)).body(containsString("Bearer "));

        String jsonString = res.body(containsString("")).extract().response().getBody().asString();

        JsonNode jsonNode = new ObjectMapper().readTree(jsonString);
        token = jsonNode.get("data").get("token").asText();
    }


    @Test
    public void shouldAddAssistant() {


        String url = ("http://localhost:" + port + "/api/v1/owner/add-assistant");
        String payload = "{\"name\":\"John Doe\",\"password\":\"strongpassword123\",\"email\":\"johndoe@example.com\",\"phoneNumber\":\"254711516786\",\"branch\":\"test1\"}";

        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).body(payload).when()
                .post(url)
                .then()
                .statusCode(is(201));

        String jsonString = res.body(containsString("")).extract().response().getBody().asString();

        AddAssistantResponse response = Utils.parseJsonString(jsonString,AddAssistantResponse.class);

        Assistant savedAssistant = assistantRepository.findAll().get(0);

        assertEquals(savedAssistant.getName(), "John Doe");
        // ensure the tenant password is encrypted.
        assertTrue(savedAssistant.getPassword().length() > 30);
        Owner owner = ownerRepository.findAll().get(0);
        //ensure there is linking.
        assertEquals(owner.getEmail(), savedAssistant.getOwner().getEmail());

        assertEquals(response.getMessage(),"Assistant added successfully!");
        assertEquals(response.getData().getName(),"John Doe");
        assertEquals(response.getData().getEmail(),"johndoe@example.com");

    }


//    @Test
//    public void shouldRefuseTenantAdditionIflandlordDoesNotOwnUnit() throws IOException {
//        addUnit("Eden", "A1", "pidddfd");
//
//        Apartment apartment = apartmentRespository.findAll().get(0);
//        Landlord landlord = apartment.getLandlord();
//        landlord.setEmail("diff@email.com");
//        landLordRepository.save(landlord);
//
//        long id = unitRepository.findAll().get(0).getId();
//
//        String url = ("http://localhost:" + port + "/api/v1/landlord/add-tenant?unit_id=" + id);
//        String payload = "{\"name\":\"John Doe\",\"password\":\"strongpassword123\",\"email\":\"johndoe@example.com\",\"phoneNumber\":\"254711516786\"}";
//
//        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).body(payload).when()
//                .post(url)
//                .then()
//                .statusCode(is(400))
//                .body(containsString("You don't own this unit.Only the owner can add Tenant.!"));
//
//
//    }
//
//    @Test
//    public void shouldGetApartments() throws IOException {
//        registerApartment("Apartment1");
//        registerApartment("Apartment2");
//
//
//        String url = ("http://localhost:" + port + "/api/v1/landlord/get-apartments");
//
//        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).when()
//                .get(url)
//                .then()
//                .statusCode(is(201))
//                .body(containsString("Fetched apartments successfully!"));
//
//        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
//
//        JsonNode jsonNode = new ObjectMapper().readTree(jsonString);
//        assertEquals(jsonNode.get("message").asText(), "Fetched apartments successfully!");
//        assertTrue(jsonNode.get("data").toString().contains("Apartment1"));
//        assertTrue(jsonNode.get("data").toString().contains("Apartment2"));
//
//    }
//
//    @Test
//    public void shouldGetUnits() throws IOException {
//        addUnit("Eden", "A1", "device1");
//        addUnit("Eden", "A2", "device2");
//        addUnit("Eden", "A3", "device3");
//
//        Apartment apartment = apartmentRespository.findAll().get(0);
//        String url = ("http://localhost:" + port + "/api/v1/landlord/get-units?apartment_id=" + apartment.getId());
//
//        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).when()
//                .get(url)
//                .then()
//                .statusCode(is(201))
//                .body(containsString("Fetched units successfully!"));
//
//        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
//
//        JsonNode jsonNode = new ObjectMapper().readTree(jsonString);
//        assertEquals(jsonNode.get("message").asText(), "Fetched units successfully!");
//        assertTrue(jsonNode.get("data").toString().contains("A1"));
//        assertTrue(jsonNode.get("data").toString().contains("A2"));
//        assertTrue(jsonNode.get("data").toString().contains("A3"));
//
//
//    }
//
//
//    @Test
//    public void shouldGetTenants() throws IOException {
//        addUnit("Eden", "A1", "derive1");
//        addUnit("Eden", "A2", "device2");
//        addUnit("Eden", "A3", "device3");
//
//        Apartment apartment = apartmentRespository.findAll().get(0);
//        String url = ("http://localhost:" + port + "/api/v1/landlord/get-units?apartment_id=" + apartment.getId());
//
//        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).when()
//                .get(url)
//                .then()
//                .statusCode(is(201))
//                .body(containsString("Fetched units successfully!"));
//
//        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
//
//        JsonNode jsonNode = new ObjectMapper().readTree(jsonString);
//        assertEquals(jsonNode.get("message").asText(), "Fetched units successfully!");
//        assertTrue(jsonNode.get("data").toString().contains("A1"));
//        assertTrue(jsonNode.get("data").toString().contains("A2"));
//        assertTrue(jsonNode.get("data").toString().contains("A3"));
//
//
//    }


    @Data

    public static class AddAssistantResponse {

        @JsonProperty("message")
        private String message;

        @JsonProperty("data")
        private AssistantData data;
        @Data
        public static class AssistantData {

            @JsonProperty("id")
            private Long id;

            @JsonProperty("name")
            private String name;

            @JsonProperty("password") // Security risk to include password in response, consider removing
            private String password;

            @JsonProperty("email")
            private String email;

            @JsonProperty("role")
            private String role;

            @JsonProperty("phoneNumber")
            private String phoneNumber;

            @JsonProperty("branch")
            private String branch;

            @JsonProperty("verified")
            private boolean verified;

            @JsonProperty("enabled")
            private boolean enabled;
            @JsonProperty("authorities")
            private List<Authority> authorities;

            @Data
            public static class Authority {

                @JsonProperty("authority")
                private String authority;
            }

            @JsonProperty("username")
            private String username;

            @JsonProperty("accountNonExpired")
            private boolean accountNonExpired;

            @JsonProperty("accountNonLocked")
            private boolean accountNonLocked;

            @JsonProperty("credentialsNonExpired")
            private boolean credentialsNonExpired;
        }
    }

}
