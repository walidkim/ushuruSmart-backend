package com.emtech.ushurusmart.usermanagement.controller;

import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.restassured.response.ValidatableResponse;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConfiguration(value = "")
public class OwnerAuthControllerTest {

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
    public void shouldSignUp() {

        String signupRequestJson = "{\"name\":\"John Doe\",\"password\":\"strongpassword123\",\"email\":\"johndoe@example.com\"}";
        given().header("Content-Type",
                        "application/json").body(signupRequestJson).when()
                .post(signUpUrl)
                .then()
                .statusCode(is(201))
                .body(containsString("Owner created successfully!"));

         Owner owner = ownerRepository.findByEmail("johndoe@example.com");
        Assertions.assertNotNull(owner);
        Assertions.assertTrue(owner.getPassword().length() > 30);
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
    public void shouldNotSignUpIfEmailExisting() {
        signUp("John Doe", "johndoe@example.com", "strongpassword123");

        String signupRequestJson = "{\"name\":\"John Doe\",\"password\":\"strongpassword123\",\"email\":\"johndoe@example.com\"}";
        given().header("Content-Type",
                        "application/json").body(signupRequestJson).when()
                .post(signUpUrl)
                .then()
                .statusCode(is(400))
                .body(containsString("Owner with that email exists!"));
    }

    @Test
    public void shouldLogin() throws IOException {
        // first sign up.
        signUp("John Doe", "johndoe@example.com", "strongpassword123");
        String loginJson = "{\"email\":\"johndoe@example.com\",\"password\":\"strongpassword123\"}";
        ValidatableResponse res = given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(201));


        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        LoginResponse response = Utils.parseJsonString(jsonString, LoginResponse.class);
        assertEquals(response.getMessage(), "Login successful!");

        LoginResponse.DataResponse resData = response.getData();
        assertTrue(resData.getToken().contains("Bearer "));
        assertTrue(resData.getToken().length() > 50);
        LoginResponse.DataResponse.OwnerData ownerData = resData.getOwner();
        assertNotNull(ownerData);
        assertEquals(ownerData.getName(), "John Doe");
    }

    @Test
    public void shouldNotifyNonExistingEmailLogin() {

        String loginJson = "{\"email\":\"johndoe@example.com\",\"password\":\"strongpassword123\"}";
        given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(404))
                .body(containsString("No Owner by that email exists."));

    }

    @Test
    public void shouldNotLoginWithWrongEmail() {
        signUp("John Doe", "johndoe@example.com", "strongpassword123");
        signUp("John Doe", "johndoe2@example.com", "strongpassword1234");

        String loginJson = "{\"email\":\"johndoe2@example.com\",\"password\":\"strongpassword123\"}";
        given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(401))
                .body(containsString("Invalid email or password."));

    }

    @Test
    public void shouldNotLoginWithWrongPassword() {
        signUp("John Doe", "johndoe@example.com", "strongpassword123");

        String loginJson = "{\"email\":\"johndoe@example.com\",\"password\":\"strongpassword1234\"}";
        given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(401))
                .body(containsString("Invalid email or password."));

    }

    @Data
    public static class LoginResponse {

        @JsonProperty("message")
        private String message;

        @JsonProperty("data")
        private DataResponse data;

        @Data
        public static class DataResponse {

            @JsonProperty("owner")
            private OwnerData owner;

            @JsonProperty("token")
            private String token;


            @Data
            public class OwnerData {
                @JsonProperty("id")
                private String id;

              @JsonProperty("name")
                private String name;

                @JsonProperty("email")
                private String email;

                @JsonProperty("businessKRAPin")
                private String businessKRAPin;

                @JsonProperty("businessOwnerKRAPin")
                private String businessOwnerKRAPin;

                @JsonProperty("phoneNumber")
                private String phoneNumber;


            }


        }
    }


}
