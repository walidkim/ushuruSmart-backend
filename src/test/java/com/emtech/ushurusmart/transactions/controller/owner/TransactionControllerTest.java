package com.emtech.ushurusmart.transactions.controller.owner;

import com.emtech.ushurusmart.UshuruSmartApplication;
import com.emtech.ushurusmart.etims.entity.Transaction;
import com.emtech.ushurusmart.etims.repository.EtimsRepository;
import com.emtech.ushurusmart.etims.repository.TransactionRepository;
import com.emtech.ushurusmart.etims.service.SampleDataInitializer;
import com.emtech.ushurusmart.etims_middleware.TransactionMiddleware;
import com.emtech.ushurusmart.transactions.entity.Product;
import com.emtech.ushurusmart.transactions.factory.EntityFactory;
import com.emtech.ushurusmart.transactions.repository.ProductRepository;
import com.emtech.ushurusmart.usermanagement.Dtos.entity.ProductDto;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.model.Role;
import com.emtech.ushurusmart.usermanagement.repository.AssistantRepository;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.utils.otp.OtpRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = UshuruSmartApplication.class)
public class TransactionControllerTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OwnerRepository ownerRepository;


    @Autowired
    private EtimsRepository etimsRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private AssistantRepository assistantRepository;
    @Autowired
    private SampleDataInitializer sampleDataInitializer;


    @Autowired
    private OtpRepository otpRepository;


    @MockBean
    private TransactionMiddleware transactionMiddleware;


    @LocalServerPort
    private int port;

    private String token;

    private String loginUrl;

    private String verifyOtpUrl;


    @BeforeEach
    public void setup() throws IOException {
        transactionRepository.deleteAll();
        etimsRepository.deleteAll();
        productRepository.deleteAll();
        assistantRepository.deleteAll();
        ownerRepository.deleteAll();


        loginUrl = "http://localhost:" + port + "/api/v1/auth/login?type=owner";
        verifyOtpUrl = "http://localhost:" + port + "/api/v1/auth/verify-otp";
        loginAndGetToken();
        sampleDataInitializer.initSampleData();

    }


    @Transactional
    private Product addProduct(String name) {
        Product prod = EntityFactory.createProduct(new ProductDto("test desc", name, 34, false, "pcs", "pcs", 23.45));
        prod = productRepository.save(prod);
        Owner owner = ownerRepository.findAll().get(0);
        prod.setOwner(owner);
        return productRepository.save(prod);
    }

    @AfterEach
    public void tearDown() {
        transactionRepository.deleteAll();
        etimsRepository.deleteAll();
        productRepository.deleteAll();
        assistantRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    private void loginAndGetToken() throws IOException {
        Owner owner = new Owner();
        if (ownerRepository.findAll().isEmpty()) {
            owner.setName("test");
            owner.setEmail("test@test.com");
            owner.setPassword("test");
            owner.setPhoneNumber("25489898989");
            owner.setKRAPin("A012345678B");
            owner.setBusinessKRAPin("P012345678Z");
            owner.setRole(Role.owner);
            ownerService.save(owner);
        }
        String loginJson = "{\"email\":\"test@test.com\",\"password\":\"test\"}";
        ValidatableResponse res = given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(201));
        String otpCode = otpRepository.findAll().get(0).getOtpCode();
        String verifyOtp = " {\n" +
                " \"phoneNumber\": \"25489898989\",\n" +
                " \"type\": \"owner\",\n" +
                " \"otpCode\": \"" + otpCode + "\"\n" +
                "}";
        res = given().header("Content-Type", "application/json").body(verifyOtp).when()
                .post(verifyOtpUrl)
                .then()
                .statusCode(is(201));


        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        JsonNode jsonNode = new ObjectMapper().readTree(jsonString);
        token = "Bearer " + jsonNode.get("data").get("token").asText();
    }


    @Test
    public void shouldMakeATransaction() {
        when(transactionMiddleware.makeTransaction(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());
        Product prod = addProduct("test1");
        Product prod2 = addProduct("test2");

        String url = ("http://localhost:" + port + "/api/v1/tax/make-transaction");
        String payload = "\n" +
                "{ \n" +
                " \"buyerKRAPin\": \"A12345678H\",\n" +
                " \"sales\": [\n" +
                "    {\n" +
                "      \"productId\": \"" + prod.getId() + "\",\n" +
                "      \"quantity\": 20\n" +
                "    },\n" +
                " {\n" +
                "      \"productId\": \"" + prod2.getId() + "\",\n" +
                "      \"quantity\": 30\n" +
                "    }\n" +
                " ]\n" +
                "}";
        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).body(payload).when()
                .post(url)
                .then()
                .statusCode(is(201));
        ResponseBody jsonString = res.body(containsString("")).extract().response().getBody();
        System.out.println(jsonString);

        byte[] pdfBytes = jsonString.asByteArray();

        // Save the PDF to a file
        try (OutputStream outputStream = new FileOutputStream("invoice-test.pdf")) {
            outputStream.write(pdfBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void shouldRefuseIfNotRegistered() {
        Product prod = addProduct("test1");
        Product prod2 = addProduct("test2");

        when(transactionMiddleware.makeTransaction(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        String url = ("http://localhost:" + port + "/api/v1/tax/make-transaction");
        String payload = "{\n" +
                " \"buyerKRAPin\": \"A12345678H\",\n" +
                " \"sales\": [\n" +
                "    {\n" +
                "      \"productId\": \"" + prod.getId() + "\",\n" +
                "      \"quantity\": 20\n" +
                "    },\n" +
                " {\n" +
                "      \"productId\": \"" + prod2.getId() + "\",\n" +
                "      \"quantity\": 30\n" +
                "    }\n" +
                " ]\n" +
                "}";
        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).body(payload).when()
                .post(url)
                .then()
                .statusCode(is(404));
        ResponseBody jsonString = res.body(containsString("")).extract().response().getBody();

        byte[] pdfBytes = jsonString.asByteArray();

        // Save the PDF to a file
        try (OutputStream outputStream = new FileOutputStream("invoice-test.pdf")) {
            outputStream.write(pdfBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void shouldReturnTransactionsInRange() {
        // Setup mock behavior for TransactionMiddleware
        LocalDate startDate = LocalDate.of(2024, 5, 1); // Example start date
        LocalDate endDate = LocalDate.of(2024, 5, 31); // Example end date
        List<Transaction> transactions = new ArrayList<>();
        // Populate transactions list with example data
        // This is just an example, adjust according to your actual data structure and requirements
        ResponseEntity res = ResponseEntity.status(HttpStatus.OK).body(transactions);

        when(transactionMiddleware.getRangeTransactions(startDate, endDate)).thenReturn(res);

        // Call the endpoint
        String url = "http://localhost:" + port + "/api/v1/tax/range";
        ValidatableResponse response = given()
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate).
                header("Authorization", token)
                .when()
                .get(url)
                .then()
                .statusCode(is(400));
        String jsonString = response.body(containsString("")).extract().response().getBody().asString();
        System.out.println(jsonString);

        // Verify the response body contains the expected transactions
        // Adjust the verification logic based on your actual data structure and requirements
        // For example, if transactions contain a field named "id":
        // assertThat(response.extract().jsonPath().getList(".", Transaction[].class)).hasSize(transactions.size());
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
    public static class AllProductResponse {
        @JsonProperty("message")
        private String message;
        private List<ProductResponse.ProductData> data;

    }

    @Data

    public static class ProductResponse {

        @JsonProperty("message")
        private String message;

        @JsonProperty("data")
        private ProductData data;

        @Data
        public static class ProductData {

            @JsonProperty("id")
            private Long id;

            @JsonProperty("name")
            private String name;

            @JsonProperty("unitPrice")
            private double unitPrice;

            @JsonProperty("unitOfMeasure")
            private String unitOfMeasure;

            @JsonProperty("taxExempted")
            private boolean taxExempted;

            @JsonProperty("quantity")
            private int quantity;

            @JsonProperty("description")
            private String description;

            @JsonProperty("type")
            private String type;

            @JsonProperty("dateCreated")
            private String dateCreated;

            @JsonProperty("dateUpdated")
            private String dateUpdated;

            @JsonProperty("transactions")
            private Object transactions; // Placeholder for potential transactions data
        }
    }


    @Data
    public static class ErrorResponse {

        @JsonProperty("timestamp")
        private String timestamp;

        @JsonProperty("status")
        private int status;

        @JsonProperty("error")
        private String error;

        @JsonProperty("path")
        private String path;
    }


}

