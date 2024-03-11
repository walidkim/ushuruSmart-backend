package com.emtech.ushurusmart.usermanagement.controller;


import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConfiguration(value = "application-test.properties")
public class OwnerAuthTest{

    @Autowired
    private OwnerRepository ownerRepository;


    @LocalServerPort
    private int port;

    private String signUpUrl;

    private String loginUrl;

    @BeforeEach
    public void setUp() {
        ownerRepository.deleteAll();
        signUpUrl = "http://localhost:" + port + "/api/v1/auth/sign-up?type=owner";
        loginUrl = "http://localhost:" + port + "/api/v1/auth/login?type=owner";

    }

    @AfterEach
    public void tearDown() {
        ownerRepository.deleteAll();
    }


    @Test
    public void shouldSignUp() throws IOException {
        String signupRequestJson = "{\"name\":\"John Doe\",\"password\":\"strongpassword123\",\"email\":\"johndoe@example.com\"}";
        String res= given().header("Content-Type",
                        "application/json").body(signupRequestJson).when()
                .post(signUpUrl)
                .then()
                .statusCode(is(201))
                .body(containsString("")).extract().response().getBody().asString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(res);
        Assertions.assertEquals(jsonNode.get("message").asText(),"Owner created successfully!");

        Owner auth= ownerRepository.findByEmail("johndoe@example.com");
        Assertions.assertNotNull(auth);
        Assertions.assertTrue(auth.getPassword().length()>30);
    }

    private void signUp(String name, String email, String password) {

        String signupRequestJson = String.format("{\"name\":\"%s\",\"password\":\"%s\",\"email\":\"%s\"}", name,
                password, email);
        given().header("Content-Type",
                        "application/json").body(signupRequestJson).when()
                .post(signUpUrl)
                .then()
                .statusCode(is(201));
    }

    @Test
    public void shouldNotSignUpIfEmailExisting() throws IOException {
        signUp("John Doe", "johndoe@example.com", "strongpassword123");

        String signupRequestJson = "{\"name\":\"John Doe\",\"password\":\"strongpassword123\",\"email\":\"johndoe@example.com\"}";
        ValidatableResponse res= given().header("Content-Type",
                        "application/json").body(signupRequestJson).when()
                .post(signUpUrl)
                .then()
                .statusCode(is(400))
                .body(containsString("Owner by that email! exists"));
        String jsonRes= res.extract().response().getBody().asString();
        JsonNode jsonNode =  new ObjectMapper().readTree(jsonRes);

        Assertions.assertEquals(jsonNode.get("message").asText(),"Owner by that email! exists");

    }

    @Test
    public void shouldLogin() throws IOException {
        // first sign up.
        signUp("John Doe", "johndoe@example.com", "strongpassword123");
        String loginJson = "{\"email\":\"johndoe@example.com\",\"password\":\"strongpassword123\"}";
        ValidatableResponse res= given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(201))
                .body(containsString("Bearer "));

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
                .body(containsString("No Owner by that email exists."));

         String jsonRes= res.extract().response().getBody().asString();
        JsonNode jsonNode =  new ObjectMapper().readTree(jsonRes);

        Assertions.assertEquals(jsonNode.get("message").asText(),"No Owner by that email exists.");



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
