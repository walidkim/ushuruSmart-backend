package com.emtech.ushurusmart.usermanagement.controller;


import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.repository.AssistantRepository;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import com.emtech.ushurusmart.usermanagement.service.AssistantService;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConfiguration(value = "application-test.properties")
public class AssistantAuthTest {

    @Autowired
    private AssistantRepository assistantRepository;


    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private AssistantService assistantService;


    @Autowired
    private OwnerService ownerService;



    private Owner owner;


    @LocalServerPort
    private int port;



    private String loginUrl;


    @BeforeEach
    public void setUp() {
        assistantRepository.deleteAll();
        ownerRepository.deleteAll();;
        loginUrl = "http://localhost:" + port + "/api/v1/auth/login?type=assistant";
        owner= new Owner();
        owner.setName("Example");
        owner.setEmail("example@.com");
        owner.setPassword("password");
        ownerService.save(owner);
    }

    @AfterEach
    public void tearDown() {
        assistantRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    private void signUp(String name, String email, String password) {

        Assistant assistant = new Assistant();
        assistant.setName(name);
        assistant.setEmail(email);
        assistant.setPassword(password);
        assistant.setOwner(owner);
       assistantRepository.save(assistant);
    }
    @Test
    public void shouldLogin() throws IOException {
        // first sign up.
        signUp("John Doe", "johndoe@example.com", "strongpassword123");
        String loginJson = "{\"email\":\"johndoe@example.com\",\"password\":\"strongpassword123\"}";
        ValidatableResponse res= given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(403))
                .body(containsString("Bearer "));
        System.out.println(assistantRepository.findAll());

        String jsonString = res.body(containsString("")).extract().response().getBody().asString();

        JsonNode jsonNode =  new ObjectMapper().readTree(jsonString);
        Assertions.assertEquals(jsonNode.get("message").asText(),"Login successful!");
        Assertions.assertTrue(jsonNode.get("data").get("token").asText().contains("Bearer "));
        Assertions.assertTrue(jsonNode.get("data").get("token").asToken().id()>1);
        Assertions.assertTrue(jsonNode.get("data").get("owner").has("email"));
        Assertions.assertTrue(jsonNode.get("data").get("owner").has("name"));
        Assertions.assertTrue(jsonNode.get("data").get("owner").has("phoneNumber"));






    }

    @Test
    public void shouldNotifyNonExistingEmailLogin() throws IOException {

        String loginJson = "{\"email\":\"johndoe@example.com\",\"password\":\"strongpassword123\"}";
        ValidatableResponse res= given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(404))
                .body(containsString("No Assistant by that email exists."));

        String jsonRes= res.extract().response().getBody().asString();
        JsonNode jsonNode =  new ObjectMapper().readTree(jsonRes);

        Assertions.assertEquals(jsonNode.get("message").asText(),"No Assistant by that email exists.");



    }

    @Test
    public void shouldNotLoginWithWrongEmail() throws IOException {
        signUp("John Doe", "johndoe@example.com", "strongpassword123");
        signUp("John Doe", "johndoe2@example.com", "strongpassword1234");

        String loginJson = "{\"email\":\"johndoe2@example.com\",\"password\":\"strongpassword123\"}";

        ValidatableResponse res= given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(401))
                .body(containsString("Invalid email or password."));
        String jsonRes= res.extract().response().getBody().asString();
        JsonNode jsonNode =  new ObjectMapper().readTree(jsonRes);

        Assertions.assertEquals(jsonNode.get("message").asText(),"Invalid email or password.");


    }

    @Test
    public void shouldNotLoginWithWrongPassword() throws IOException {
        signUp("John Doe", "johndoe@example.com", "strongpassword123");

        String loginJson = "{\"email\":\"johndoe@example.com\",\"password\":\"strongpassword1234\"}";
        ValidatableResponse res=given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(401))
                .body(containsString("Invalid email or password."));
        String jsonRes= res.extract().response().getBody().asString();
        JsonNode jsonNode =  new ObjectMapper().readTree(jsonRes);

        Assertions.assertEquals(jsonNode.get("message").asText(),"Invalid email or password.");

    }

}
