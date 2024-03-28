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

        Assistant assistant = new Assistant();
        Owner owner= new Owner();
        owner.setName("test");
        owner.setEmail("test@test.com");
        owner.setPassword("test");
        owner.setPhoneNumber("25489898989");
        ownerService.save(owner);
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
                .statusCode(is(401));


        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        System.out.println(jsonString);
        LoginResponse response = Utils.parseJsonString(jsonString, LoginResponse.class);
        assertEquals(response.getMessage(), "Login successful!");

        LoginResponse.DataResponse resData = response.getData();
        assertTrue(resData.getToken().contains("Bearer "));
        assertTrue(resData.getToken().length() > 50);
        LoginResponse.DataResponse.TenantData tenant = resData.getTenant();
        assertNotNull(tenant);
        assertEquals(tenant.getName(), "John Doe");
        assertEquals(tenant.getEmail(), "johndoe@example.com");


    }

    //
    @Test
    public void shouldNotifyNonExistingEmailLogin() {

        String loginJson = "{\"email\":\"johndoe@example.com\",\"password\":\"strongpassword123\"}";
        given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(404))
                .body(containsString("No tenant by that email exists."));

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
        public static class DataResponse {

            @JsonProperty("tenant")
            private TenantData tenant;

            @JsonProperty("token")
            private String token;


            @Data
            public class TenantData {

                @JsonProperty("id")
                private Long id;

                @JsonProperty("name")
                private String name;

                @JsonProperty("password")  // Security risk to include password in response, consider removing
                private String password;

                @JsonProperty("email")
                private String email;

                @JsonProperty("phoneNumber")
                private String phoneNumber;

                @JsonProperty("units")
                private List<Object> units;  // Placeholder for potential apartment data

                @JsonProperty("amountPaid")
                private Double amountPaid;

                @JsonProperty("createdAt")
                private CreatedAt createdAt;

                @JsonProperty("updatedAt")
                private Object updatedAt;  // Can be null

                @Data
                public class CreatedAt {

                    @JsonProperty("year")
                    private Integer year;

                    @JsonProperty("monthValue")
                    private Integer monthValue;

                    @JsonProperty("dayOfMonth")
                    private Integer dayOfMonth;

                    @JsonProperty("hour")
                    private Integer hour;

                    @JsonProperty("minute")
                    private Integer minute;

                    @JsonProperty("second")
                    private Integer second;

                    @JsonProperty("nano")
                    private Long nano;

                    @JsonProperty("dayOfWeek")
                    private String dayOfWeek;

                    @JsonProperty("dayOfYear")
                    private Integer dayOfYear;

                    @JsonProperty("month")
                    private String month;

                    @JsonProperty("chronology")
                    private Chronology chronology;


                    @Data
                    public class Chronology {

                        @JsonProperty("id")
                        private String id;

                        @JsonProperty("calendarType")
                        private String calendarType;
                    }
                }

            }


        }
    }

}
