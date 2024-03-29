package com.emtech.ushurusmart.transactions.controller;

import com.emtech.ushurusmart.transactions.entity.Product;
import com.emtech.ushurusmart.transactions.factory.EntityFactory;
import com.emtech.ushurusmart.transactions.repository.ProductRepository;
import com.emtech.ushurusmart.usermanagement.Dtos.entity.ProductDto;
import com.emtech.ushurusmart.usermanagement.controller.Utils;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.model.Role;
import com.emtech.ushurusmart.usermanagement.repository.AssistantRepository;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ValidatableResponse;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;




@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "")
public class ProductControllerTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OwnerRepository ownerRepository;


    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private AssistantRepository assistantRepository;


    @LocalServerPort
    private int port;

    private String token;

    private String signUpUrl;

    private String loginUrl;

    @BeforeEach
    public void setup() throws IOException {

        ownerRepository.deleteAll();

        signUpUrl = "http://localhost:" + port + "/api/v1/auth/sign-up?type=owner";
        loginUrl = "http://localhost:" + port + "/api/v1/auth/login?type=owner";
        loginAndGetToken();

    }

    @AfterEach
    public void tearDown() {
        ownerRepository.deleteAll();
    }

    private void loginAndGetToken() throws IOException {
        Owner owner = new Owner();
        if (ownerRepository.findAll().isEmpty()) {
            owner.setName("test");
            owner.setEmail("test@test.com");
            owner.setPassword("test");
            owner.setPhoneNumber("25489898989");
            owner.setRole(Role.owner);
            ownerService.save(owner);
        }
        String loginJson = "{\"email\":\"test@test.com\",\"password\":\"test\"}";
        ValidatableResponse res = given().header("Content-Type", "application/json").body(loginJson).when()
                .post(loginUrl)
                .then()
                .statusCode(is(201)).body(containsString("Bearer "));

        String jsonString = res.body(containsString("")).extract().response().getBody().asString();

        JsonNode jsonNode = new ObjectMapper().readTree(jsonString);
        token = jsonNode.get("data").get("token").asText();
    }


    @Test
    public void shouldAddProduct() {
        String url = ("http://localhost:" + port + "/api/v1/product/create");
        String payload = "{\"description\": \"This is a product with a test description containing double quotes.\", \"name\": \"Amazing Product\", \"quantity\": 10, \"taxExempted\": false, \"type\": \"Gadget\", \"unitOfMeasure\": \"pcs\", \"unitPrice\": 19.99}";
        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).body(payload).when()
                .post(url)
                .then()
                .statusCode(is(201));

        Product saved = productRepository.findAll().get(0);
        Owner owner= ownerRepository.findAll().get(0);
        assertNotNull(saved);
        assertEquals(saved.getOwner().getId(), owner.getId());
        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        ProductCreatedResponse response = Utils.parseJsonString(jsonString,ProductCreatedResponse.class);
        assertEquals(response.getMessage(), "Product created successfully!");
        ProductCreatedResponse.ProductData product= response.getData();
        assertNotNull(product);
        assertEquals(product.getName(),"Amazing Product");

    }
    @Transactional
   private Product addProduct(){
        Product prod= EntityFactory.createProduct(new ProductDto("test desc","test",34,false,"pcs","pcs",23.45));
        prod= productRepository.save(prod);
        Owner owner = ownerRepository.findAll().get(0);
       prod.setOwner(owner);
        return productRepository.save(prod);
   }



   @Test
    public void shouldModifyProduct() {
        Product prod= addProduct();
        String url = ("http://localhost:" + port + "/api/v1/product/update/"+ prod.getId());
        String payload = "{\"description\": \"This is a product with a test description containing double quotes.\", \"name\": \"Amazing Product\", \"quantity\": 10, \"taxExempted\": false, \"type\": \"Gadget\", \"unitOfMeasure\": \"pcs\", \"unitPrice\": 19.99}";
        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).body(payload).when()
                .put(url)
                .then()
                .statusCode(is(200));

        List<Product> saved = productRepository.findAll();
       assertEquals(1, saved.size());
        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        ProductCreatedResponse response = Utils.parseJsonString(jsonString,ProductCreatedResponse.class);
        assertEquals(response.getMessage(), "Product updated successfully.");
        ProductCreatedResponse.ProductData product= response.getData();
        assertNotNull(product);
        assertEquals(product.getName(),"Amazing Product");

    }


    @Test
    public void refuseUpdationOnEmpytProductsOrNonExistentProductId() {
        String url = ("http://localhost:" + port + "/api/v1/product/update/"+ 1);
        String payload = "{\"description\": \"This is a product with a test description containing double quotes.\", \"name\": \"Amazing Product\", \"quantity\": 10, \"taxExempted\": false, \"type\": \"Gadget\", \"unitOfMeasure\": \"pcs\", \"unitPrice\": 19.99}";
        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).body(payload).when()
                .put(url)
                .then()
                .statusCode(is(404));
        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        ProductCreatedResponse response = Utils.parseJsonString(jsonString,ProductCreatedResponse.class);
        assertEquals(response.getMessage(), "No product with that id exists.");
        ProductCreatedResponse.ProductData product= response.getData();
        assertNull(product);

    }


    @Test
    public void shouldRefuseIfOwnerDoesNotOwnProduct() {
        Product prod= addProduct();
        Owner owner= new Owner();
        owner.setName("test2");
        owner.setEmail("test2@test.com");
        owner.setPassword("test2");
        owner.setPhoneNumber("25489898989");
        owner.setRole(Role.owner);
        ownerService.save(owner);
        prod.setOwner(owner);
        productRepository.save(prod);
        String url = ("http://localhost:" + port + "/api/v1/product/update/"+ prod.getId());
        String payload = "{\"description\": \"This is a product with a test description containing double quotes.\", \"name\": \"Amazing Product\", \"quantity\": 10, \"taxExempted\": false, \"type\": \"Gadget\", \"unitOfMeasure\": \"pcs\", \"unitPrice\": 19.99}";
        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).body(payload).when()
                .put(url)
                .then()
                .statusCode(is(401));

        List<Product> saved = productRepository.findAll();
        assertEquals(1, saved.size());
        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        ProductCreatedResponse response = Utils.parseJsonString(jsonString,ProductCreatedResponse.class);
        assertEquals(response.getMessage(), "You are not authorized to modify this product.");
        ProductCreatedResponse.ProductData product= response.getData();
        assertNull(product);

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

    public static class ProductCreatedResponse {

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


}
