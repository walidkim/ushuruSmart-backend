package com.emtech.ushurusmart.usermanagement.controller.owner;

import com.emtech.ushurusmart.etims_middleware.EtimsMiddleware;
import com.emtech.ushurusmart.usermanagement.Dtos.auth.OtpDataDto;
import com.emtech.ushurusmart.usermanagement.controller.Utils;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.model.Role;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.utils.otp.OTPService;
import com.emtech.ushurusmart.utils.otp.OtpRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OwnerAuthControllerTest {

    @Autowired
    private OwnerRepository ownerRepository;


    @Autowired
    private OtpRepository otpRepository;

    @MockBean
    private EtimsMiddleware etimsMiddleware;

    @Autowired
    private OwnerService ownerService;

    @LocalServerPort
    private int port;

    private String signUpUrl;

    private String loginUrl;


    private String verifyOtpUrl;





    @Mock
    private OTPService otpService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ownerRepository.deleteAll();
        otpRepository.deleteAll();
        signUpUrl = "http://localhost:" + port + "/api/v1/auth/sign-up?type=owner";
        loginUrl = "http://localhost:" + port + "/api/v1/auth/login?type=owner";
        verifyOtpUrl= "http://localhost:" + port + "/api/v1/auth/verify-otp";

    }

    @AfterEach
    public void tearDown() {
        ownerRepository.deleteAll();
        otpRepository.deleteAll();
    }


    @Test
    public void shouldSignUpIfRegisteredInEtims() {
        when(etimsMiddleware.verifyBusinessKRAPin(any())).thenReturn(ResponseEntity.status(HttpStatus.FOUND).build());

        String signupRequestJson = "{\n" +
                " \"businessKRAPin\": \"123456\",\n" +
                " \"businessOwnerKRAPin\": \"789012\",\n" +
                " \"email\": \"testuser@example.com\",\n" +
                " \"name\": \"John Doe\",\n" +
                " \"password\": \"SecurePassword123\",\n" +
                " \"phoneNumber\": \"+25474567890\"\n" +
                "}";

        ValidatableResponse res= given().header("Content-Type",
                        "application/json").body(signupRequestJson).when()
                .post(signUpUrl)
                .then()
                .statusCode(is(201));
        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        SignUpResponse response=  Utils.parseJsonString(jsonString,SignUpResponse.class);
        assert response != null;
        assertEquals(response.getMessage(),"Owner created successfully!");
        List<Owner> foundOwners= ownerRepository.findAll();
        assertEquals(foundOwners.size(),1);
        Owner savedOwner= foundOwners.get(0);

        Assertions.assertNotNull(savedOwner);
        Assertions.assertTrue(savedOwner.getPassword().length() > 30);

        SignUpResponse.BodyData payloadOwner= response.getData();
        assertEquals(payloadOwner.getId(), savedOwner.getId());
        assertEquals(payloadOwner.getEmail(), savedOwner.getEmail());

    }



    @Test
    public void shouldRefuseOtherTypesSignUps() {
        when(etimsMiddleware.verifyBusinessKRAPin(any())).thenReturn(ResponseEntity.status(HttpStatus.FOUND).build());

        String signupRequestJson = "{\n" +
                " \"businessKRAPin\": \"123456\",\n" +
                " \"businessOwnerKRAPin\": \"789012\",\n" +
                " \"email\": \"testuser@example.com\",\n" +
                " \"name\": \"John Doe\",\n" +
                " \"password\": \"SecurePassword123\",\n" +
                " \"phoneNumber\": \"+25474567890\"\n" +
                "}";

        ValidatableResponse res= given().header("Content-Type",
                        "application/json").body(signupRequestJson).when()
                .post("http://localhost:" + port + "/api/v1/auth/sign-up?type=random")
                .then()
                .statusCode(is(400));
        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        SignUpResponse response=  Utils.parseJsonString(jsonString,SignUpResponse.class);
        assert response != null;
        assertEquals(response.getMessage(),"Random is an invalid type!");

    }


    @Test
    public void shouldRefuseInvalidDataSignUps() {
        when(etimsMiddleware.verifyBusinessKRAPin(any())).thenReturn(ResponseEntity.status(HttpStatus.FOUND).build());

        String signupRequestJson = """
                {
                 "businessKRAPin": null,
                 "businessOwnerKRAPin": "789012",
                 "email": "testuser@example.com",
                 "name": "John Doe",
                 "password": "SecurePassword123",
                 "phoneNumber": "+25474567890"
                }""";

        ValidatableResponse res= given().header("Content-Type",
                        "application/json").body(signupRequestJson).when()
                .post("http://localhost:" + port + "/api/v1/auth/sign-up?type=owner")
                .then()
                .statusCode(is(400));

    }






    private  Owner signUp(String name, String email, String password, String tel) {
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
        return owner;
    }
    @Test
    public void shouldLogin() throws IOException {
        // first sign up.
        signUp("John Doe", "johndoe@example.com", "strongpassword123","+1234567890");
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
    public void shouldVerifyOtp() {
        // Arrange


        signUp("John Doe", "johndoe@example.com", "strongpassword123","+1234567890");
        for(int i= 0; i<=5; i++){
            String loginJson = "{\"email\":\"johndoe@example.com\",\"password\":\"strongpassword123\"}";
            given().header("Content-Type", "application/json").body(loginJson).when()
                    .post(loginUrl)
                    .then()
                    .statusCode(is(201));
            String phoneNumber = ownerRepository.findAll().get(0).getPhoneNumber();
            String otpCode = otpRepository.findAll().get(0).getOtpCode();
            String type = "owner";



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
            assertTrue(response.getData().getToken().length()>50);
        }
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
        OtpVerification response = Utils.parseJsonString(jsonString,OtpVerification.class);
        assert response != null;
        assertEquals("Invalid email or password.", response.getMessage());
        assertNull(response.getData());

    }




    @Data
    public static class SignUpResponse {
        private String message;
        @JsonProperty("data")
        private BodyData data;

        @Data
        public static class BodyData {
            private int id;
            private String name;
            private String email;
            private String phoneNumber;
        }
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

            @JsonProperty("user")
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
