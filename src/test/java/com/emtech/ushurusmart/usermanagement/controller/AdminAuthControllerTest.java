package com.emtech.ushurusmart.usermanagement.controller;


import com.emtech.ushurusmart.usermanagement.model.Admin;
import com.emtech.ushurusmart.usermanagement.repository.AdminRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConfiguration(value = "application-test.properties")
public class AdminAuthControllerTest {

    @Autowired
    private AdminRepository adminRepository;


    @LocalServerPort
    private int port;

    private String signUpUrl;

    private String loginUrl;

    @BeforeEach
    public void setUp() {
        adminRepository.deleteAll();
        signUpUrl = "http://localhost:" + port + "/api/v1/auth/sign-up?type=admin";
        loginUrl = "http://localhost:" + port + "/api/v1/auth/login?type=admin";

    }

    @AfterEach
    public void tearDown() {
        adminRepository.deleteAll();
    }


    @Test
    public void shouldSignUp() {
        System.out.println(signUpUrl);
        String signupRequestJson = "{\"name\":\"John Doe\",\"password\":\"strongpassword123\",\"email\":\"johndoe@example.com\"}";
        given().header("Content-Type",
                        "application/json").body(signupRequestJson).when()
                .post(signUpUrl)
                .then()
                .statusCode(is(400))
                .body(containsString("Landlord created successfully!"));

        Admin auth= adminRepository.findByEmail("johndoe@example.com");
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
    public void shouldNotSignUpIfEmailExisting() {
        signUp("John Doe", "johndoe@example.com", "strongpassword123");

        String signupRequestJson = "{\"name\":\"John Doe\",\"password\":\"strongpassword123\",\"email\":\"johndoe@example.com\"}";
        given().header("Content-Type",
                        "application/json").body(signupRequestJson).when()
                .post(signUpUrl)
                .then()
                .statusCode(is(400))
                .body(containsString("Landlord with that email exists!"));
    }

    @Test
    public void shouldLogin() {
        // first sign up.
        signUp("John Doe", "johndoe@example.com", "strongpassword123");
        String loginJson = "{\"email\":\"johndoe@example.com\",\"password\":\"strongpassword123\"}";
        given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(201))
                .body(containsString("Bearer "));

    }

    @Test
    public void shouldNotifyNonExistingEmailLogin() {

        String loginJson = "{\"email\":\"johndoe@example.com\",\"password\":\"strongpassword123\"}";
        given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(404))
                .body(containsString("No landlord by that email exists."));

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

}
