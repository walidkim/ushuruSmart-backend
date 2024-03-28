package com.emtech.ushurusmart.usermanagement.controller;

import com.emtech.ushurusmart.usermanagement.controller.Utils;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.model.Role;
import com.emtech.ushurusmart.usermanagement.repository.AssistantRepository;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import com.emtech.ushurusmart.usermanagement.service.AssistantService;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.restassured.response.ValidatableResponse;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
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

    @LocalServerPort
    private int port;


    private String loginUrl;

    @BeforeEach
    public void setUp() {
        assistantRepository.deleteAll();
       ownerRepository.deleteAll();
        loginUrl = "http://localhost:" + port + "/api/v1/auth/login?type=assistant";

    }

    @AfterEach
    public void tearDown() {
        assistantRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    private void signUp(String name, String email, String password, String tel) {
        Owner owner= new Owner();
        if(ownerRepository.findAll().isEmpty()){
            owner.setName("test");
            owner.setEmail("test@test.com");
            owner.setPassword("test");
            owner.setPhoneNumber("25489898989");
            owner.setRole(Role.owner);
            ownerService.save(owner);
        }else{
            owner= ownerRepository.findAll().get(0);
        }
        Assistant assistant = new Assistant();
        assistant.setName(name);
        assistant.setEmail(email);
        assistant.setPassword(password);
        assistant.setPhoneNumber(tel);
        assistant.setRole(Role.assistant);
        assistant.setOwner(owner);
        assistantService.save(assistant);

    }

    //    @Test
//    public void shouldNotSignUpIfEmailExisting() {
//        signUp("John Doe", "johndoe@example.com", "strongpassword123");
//
//        String signupRequestJson = "{\"name\":\"John Doe\",\"password\":\"strongpassword123\",\"email\":\"johndoe@example.com\"}";
//        given().header("Content-Type",
//                        "application/json").body(signupRequestJson).when()
//                .post(signUpUrl)
//                .then()
//                .statusCode(is(400))
//                .body(containsString("Landlord with that email exists!"));
//    }
//
    @Test
    public void shouldLogin() throws IOException {
        // first sign up.
        signUp("John Doe", "johndoe@example.com", "strongpassword123", "254711516786");
        String loginJson = "{\"email\":\"johndoe@example.com\",\"password\":\"strongpassword123\"}";
        ValidatableResponse res = given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(201));


        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        System.out.println(jsonString);
        LoginResponse response = Utils.parseJsonString(jsonString, LoginResponse.class);
        assertEquals(response.getMessage(), "Login successful!");

        LoginResponse.DataResponse resData = response.getData();
        assertTrue(resData.getToken().contains("Bearer "));
        assertTrue(resData.getToken().length() > 50);
        LoginResponse.DataResponse.Assistant assistant = resData.getAssistant();
        assertNotNull(assistant);
        assertEquals(assistant.getName(), "John Doe");
        assertEquals(assistant.getEmail(), "johndoe@example.com");


    }

    //
    @Test
    public void shouldNotifyNonExistingEmailLogin() {

        String loginJson = "{\"email\":\"johndoe@example.com\",\"password\":\"strongpassword123\"}";
        given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(404))
                .body(containsString("No Assistant by that email exists"));

    }

    @Test
    public void shouldNotLoginWithWrongEmail() {
        signUp("John Doe", "johndoe@example.com", "strongpassword123", "233768888777");
        signUp("John Doe", "johndoe2@example.com", "strongpassword1234", "233768888777");

        String loginJson = "{\"email\":\"johndoe2@example.com\",\"password\":\"strongpassword123\"}";
        given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(401))
                .body(containsString("Invalid email or password."));


    }

    @Test
    public void shouldNotLoginWithWrongPassword() {
        signUp("John Doe", "johndoe@example.com", "strongpassword123", "25471176762763");

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
        public static  class DataResponse {

            @JsonProperty("assistant")
            private Assistant assistant;

            @JsonProperty("token")
            private String token;
            @Data
            public static class Assistant {

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
                private Object branch;  // Placeholder for potential branch data

                @JsonProperty("verified")
                private boolean verified;

                @JsonProperty("enabled")
                private boolean enabled;

                @JsonProperty("accountNonExpired")
                private boolean accountNonExpired;

                @JsonProperty("accountNonLocked")
                private boolean accountNonLocked;

                @JsonProperty("credentialsNonExpired")
                private boolean credentialsNonExpired;

                @JsonProperty("authorities")
                private List<Authority> authorities;

                @JsonProperty("username")
                private String username;

                @Data
                public static class Authority {

                    @JsonProperty("authority")
                    private String authority;
                }
            }
        }
    }







}
