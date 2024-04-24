package com.emtech.ushurusmart.usermanagement.controller.owner;

import com.emtech.ushurusmart.etims_middleware.EtimsMiddleware;
import com.emtech.ushurusmart.usermanagement.controller.Utils;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.utils.otp.OtpRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.restassured.response.ValidatableResponse;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestConfiguration(value = "")
public class OwnerAuthControllerTest {

    @Autowired
    private OwnerRepository ownerRepository;


    @Autowired
    private OtpRepository otpRepository;

    @MockBean
    private EtimsMiddleware etimsMiddleware;

    @InjectMocks
    private OwnerService userService;

    @LocalServerPort
    private int port;

    private String signUpUrl;

    private String loginUrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ownerRepository.deleteAll();
        otpRepository.deleteAll();
        signUpUrl = "http://localhost:" + port + "/api/v1/auth/sign-up?type=owner";
        loginUrl = "http://localhost:" + port + "/api/v1/auth/login?type=owner";

    }

    @AfterEach
    public void tearDown() {
        ownerRepository.deleteAll();
        otpRepository.deleteAll();
    }


    @Test
    public void shouldSignUp() {
        when(etimsMiddleware.verifyBusinessKRAPin(any())).thenReturn(ResponseEntity.status(HttpStatus.FOUND).build());

        String signupRequestJson = "{\n" +
                " \"businessKRAPin\": \"123456\",\n" +
                " \"businessOwnerKRAPin\": \"789012\",\n" +
                " \"email\": \"testuser@example.com\",\n" +
                " \"name\": \"John Doe\",\n" +
                " \"password\": \"SecurePassword123\",\n" +
                " \"phoneNumber\": \"+1234567890\"\n" +
                "}";

        ValidatableResponse res= given().header("Content-Type",
                        "application/json").body(signupRequestJson).when()
                .post(signUpUrl)
                .then()
                .statusCode(is(201));
        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        System.out.println(jsonString);




        Owner owner = ownerRepository.findByEmail("johndoe@example.com");
        Assertions.assertNotNull(owner);
        Assertions.assertTrue(owner.getPassword().length() > 30);
    }

    private void signUp(String name, String email, String password) {

        String signupRequestJson = String.format("{\"name\":\"%s\",\"password\":\"%s\",\"email\":\"%s\",\"businessKRAPin\":\"123456789\",\"businessOwnerKRAPin\":\"987654321\",\"phoneNumber\":\"+1234567890\"}", name, password, email);
        given().header("Content-Type",
                        "application/json").body(signupRequestJson).when()
                .post(signUpUrl)
                .then()
                .statusCode(is(201));
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
        assert response != null;
        assertEquals(response.getMessage(), "A short code has been sent to your phone for verification");


        LoginResponse.VerificationData resData = response.getData();
        assertEquals(resData.getPhoneNumber(), ("+1234567890"));
        assertEquals(resData.getType(), "owner");

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
        private VerificationData data;

        @Data
        public static class VerificationData {
            @JsonProperty("phoneNumber")
            private String phoneNumber;

            @JsonProperty("type")
            private String type;
        }
    }

    @Data
    public static class OtpVerification {

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
