package com.emtech.ushurusmart.usermanagement.controller.assistant;

import com.emtech.ushurusmart.usermanagement.Dtos.auth.OtpDataDto;
import com.emtech.ushurusmart.usermanagement.controller.Utils;
import com.emtech.ushurusmart.usermanagement.controller.owner.OwnerAuthControllerTest;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.model.Role;
import com.emtech.ushurusmart.usermanagement.repository.AssistantRepository;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import com.emtech.ushurusmart.usermanagement.service.AssistantService;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.utils.otp.OtpRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

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


    @Autowired
    private OtpRepository otpRepository;

    @LocalServerPort
    private int port;


    private String loginUrl;

    private String verifyOtpUrl;


    @BeforeEach
    public void setUp() {
        assistantRepository.deleteAll();
       ownerRepository.deleteAll();
       otpRepository.deleteAll();
        loginUrl = "http://localhost:" + port + "/api/v1/auth/login?type=assistant";
        verifyOtpUrl= "http://localhost:" + port + "/api/v1/auth/verify-otp";


    }

    @AfterEach
    public void tearDown() {
        assistantRepository.deleteAll();
        ownerRepository.deleteAll();
        otpRepository.deleteAll();
    }

    private void signUp(String name, String email, String password, String tel) {
        Owner owner= new Owner();
        if(ownerRepository.findAll().isEmpty()){
            owner.setName(name);
            owner.setEmail(email);
            owner.setPassword(password);
            owner.setPhoneNumber(tel);
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

    @Test
    public void shouldLogin() throws IOException {
        signUp("John Doe", "johndoe@example.com", "strongpassword123","+1234567890");
        String loginJson = "{\"email\":\"johndoe@example.com\",\"password\":\"strongpassword123\"}";
        ValidatableResponse res = given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(201));


        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        OwnerAuthControllerTest.LoginResponse response = Utils.parseJsonString(jsonString, OwnerAuthControllerTest.LoginResponse.class);
        assert response != null;
        assertEquals(response.getMessage(), "A short code has been sent to your phone for verification");


        OwnerAuthControllerTest.LoginResponse.VerificationData resData = response.getData();
        assertEquals(resData.getPhoneNumber(), ("+1234567890"));
        assertEquals(resData.getType(), "assistant");


    }

    @Test
    public void shouldVerifyOtp() {
        // Arrange
        signUp("John Doe", "johndoe@example.com", "strongpassword123","1234567890");
        String loginJson = "{\"email\":\"johndoe@example.com\",\"password\":\"strongpassword123\"}";
        given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(201));
        String phoneNumber = ownerRepository.findAll().get(0).getPhoneNumber();
        String otpCode = otpRepository.findAll().get(0).getOtpCode();
        String type = "assistant";



        // Prepare the request body
        OtpDataDto otpDataDto = new OtpDataDto();
        otpDataDto.setPhoneNumber(phoneNumber);
        otpDataDto.setOtpCode(otpCode);
        otpDataDto.setType(type);

        // Act
        ValidatableResponse res = given()
                .contentType(ContentType.JSON)
                .body(otpDataDto)
                .when()
                .post(verifyOtpUrl)
                .then()
                .statusCode(is(HttpStatus.CREATED.value()));

        // Assert
        String jsonString = res.extract().response().getBody().asString();
        OtpVerification response = Utils.parseJsonString(jsonString,OtpVerification.class);
        assert response != null;
        assertEquals("Login successful!", response.getMessage());
        assertTrue(response.getData().getToken().length()> 50);
    }


    @Test
    public void shouldRefuseInvalidOtp() {
        // Arrange


        signUp("John Doe", "johndoe@example.com", "strongpassword123","+1234567890");
        String loginJson = "{\"email\":\"johndoe@example.com\",\"password\":\"strongpassword123\"}";
        given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(201));
        String phoneNumber = ownerRepository.findAll().get(0).getPhoneNumber();
        String otpCode = otpRepository.findAll().get(0).getOtpCode();
        String type = "owner";
        char[] otpArray = otpCode.toCharArray();


        //Used to generate a different otpCode
        // Get the last digit
        char lastDigit = otpArray[otpArray.length - 1];

        // Determine a new digit that is different from the last digit
        char newDigit = '0'; // Initialize with a default value
        for (char digit = '0'; digit <= '9'; digit++) {
            if (digit != lastDigit) {
                newDigit = digit;
                break; // Exit the loop once a different digit is found
            }
        }

        // Replace the last digit with the new one
        otpArray[otpArray.length - 1] = newDigit;

        // Convert the character array back to a string
        String newOtpCode = new String(otpArray);


        // Prepare the request body
        OtpDataDto otpDataDto = new OtpDataDto();
        otpDataDto.setPhoneNumber(phoneNumber);
        otpDataDto.setOtpCode(newOtpCode);
        otpDataDto.setType(type);

        // Act
        ValidatableResponse res = given()
                .contentType(ContentType.JSON)
                .body(otpDataDto)
                .when()
                .post(verifyOtpUrl)
                .then()
                .statusCode(is(HttpStatus.UNAUTHORIZED.value()));

        // Assert
        String jsonString = res.extract().response().getBody().asString();
        OwnerAuthControllerTest.OtpVerification response = Utils.parseJsonString(jsonString, OwnerAuthControllerTest.OtpVerification.class);
        assert response != null;
        assertEquals("Invalid email or password.", response.getMessage());
        assertNull(response.getData());

    }



    @Data
    public static class OtpVerification {

        @JsonProperty("message")
        private String message;

        @JsonProperty("data")
        private DataResponse data;

        @Data
        public static class DataResponse {

            @JsonProperty("assistant")
            private AssistantData assistant;

            @JsonProperty("token")
            private String token;


            @Data
            public static class AssistantData {
                @JsonProperty("id")
                private String id;

                @JsonProperty("name")
                private String name;

                @JsonProperty("email")
                private String email;

                @JsonProperty("phoneNumber")
                private String phoneNumber;

            }


        }
    }







}
