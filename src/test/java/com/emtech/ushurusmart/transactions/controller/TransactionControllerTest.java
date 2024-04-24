package com.emtech.ushurusmart.transactions.controller;

import com.emtech.ushurusmart.UshuruSmartApplication;
import com.emtech.ushurusmart.etims.repository.EtimsRepository;
import com.emtech.ushurusmart.etims.repository.TransactionRepository;
import com.emtech.ushurusmart.transactions.entity.Product;
import com.emtech.ushurusmart.transactions.factory.EntityFactory;
import com.emtech.ushurusmart.transactions.repository.ProductRepository;
import com.emtech.ushurusmart.usermanagement.Dtos.entity.ProductDto;
import com.emtech.ushurusmart.usermanagement.controller.Utils;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.model.Role;
import com.emtech.ushurusmart.usermanagement.repository.AssistantRepository;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.usermanagement.service.SampleDataInitializer;
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
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;




@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = UshuruSmartApplication.class)
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
        String otpCode= otpRepository.findAll().get(0).getOtpCode();
        String verifyOtp= "{\n" +
                " \"phoneNumber\": \"25489898989\",\n" +
                " \"type\": \"owner\",\n" +
                " \"otpCode\": \""+otpCode+"\"\n" +
                "}";
        res = given().header("Content-Type", "application/json").body(verifyOtp).when()
                .post(verifyOtpUrl)
                .then()
                .statusCode(is(201));



        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        JsonNode jsonNode = new ObjectMapper().readTree(jsonString);
        token = jsonNode.get("data").get("token").asText();
    }



    @Test
    public void shouldMakeATransaction() {
        Product prod= addProduct("test1");
        String url = ("http://localhost:" + port + "/api/v1/tax/make-transaction");
        String payload = "{\n" +
                " \"buyerKRAPin\": \"A12345678H\",\n" +
                " \"sales\": [\n" +
                "    {\n" +
                "      \"productId\": \""+prod.getId()+ "\",\n" +
                "      \"quantity\": 20\n" +
                "    }\n" +
                " ]\n" +
                "}";
        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).body(payload).when()
                .post(url)
                .then()
                .statusCode(is(400));
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
    public void shouldRefuseAddProductIfOwnerNotLoggedIn() {
        String url = ("http://localhost:" + port + "/api/v1/product/create");
        String payload = "{\"description\": \"This is a product with a test description containing double quotes.\", \"name\": \"Amazing Product\", \"quantity\": 10, \"taxExempted\": false, \"type\": \"Gadget\", \"unitOfMeasure\": \"pcs\", \"unitPrice\": 19.99}";
        ValidatableResponse res = given().header("Content-Type", "application/json").body(payload).when()
                .post(url)
                .then()
                .statusCode(is(403));

        List<Product> saved = productRepository.findAll();
        assertEquals(saved.size(),0);

        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        ErrorResponse response = Utils.parseJsonString(jsonString,ErrorResponse.class);
        assertEquals(response.getError(), "Forbidden");
        assertEquals(response.getPath(),"/api/v1/product/create");

    }
    @Transactional
    private Product addProduct(){
        Product prod= EntityFactory.createProduct(new ProductDto("test desc","test",34,false,"pcs","pcs",23.45));
        prod= productRepository.save(prod);
        Owner owner = ownerRepository.findAll().get(0);
        prod.setOwner(owner);
        return productRepository.save(prod);
    }

    @Transactional
    private Product addProduct(String name){
        Product prod= EntityFactory.createProduct(new ProductDto("test desc",name,34,false,"pcs","pcs",100));
        prod.setTaxable(true);
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
        ProductResponse response = Utils.parseJsonString(jsonString,ProductResponse.class);
        assertEquals(response.getMessage(), "Product updated successfully.");
        ProductResponse.ProductData product= response.getData();
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
        ProductResponse response = Utils.parseJsonString(jsonString,ProductResponse.class);
        assertEquals(response.getMessage(), "No product with that id exists.");
        ProductResponse.ProductData product= response.getData();
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
        String url = ("http://localhost:" + port + "/api/v1/product/delete/"+ prod.getId());
        String payload = "{\"description\": \"This is a product with a test description containing double quotes.\", \"name\": \"Amazing Product\", \"quantity\": 10, \"taxExempted\": false, \"type\": \"Gadget\", \"unitOfMeasure\": \"pcs\", \"unitPrice\": 19.99}";
        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).body(payload).when()
                .delete(url)
                .then()
                .statusCode(is(401));

        List<Product> saved = productRepository.findAll();
        assertEquals(1, saved.size());
        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        ProductResponse response = Utils.parseJsonString(jsonString,ProductResponse.class);
        assertEquals(response.getMessage(), "You are not authorized to delete this product.");
        ProductResponse.ProductData product= response.getData();
        assertNull(product);

    }

    @Test
    public void shouldGetAllProducts() {
        Product prod= addProduct("test1");
        Product prod2= addProduct("test2");
        Product prod3= addProduct("test3");

        Owner owner= new Owner();
        owner.setName("test2");
        owner.setEmail("test2@test.com");
        owner.setPassword("test2");
        owner.setPhoneNumber("25489898989");
        owner.setRole(Role.owner);
        ownerService.save(owner);
        prod.setOwner(owner);
        productRepository.save(prod);
        String url = ("http://localhost:" + port + "/api/v1/products");
        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).when()
                .get(url)
                .then()
                .statusCode(is(200));
        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        AllProductResponse response = Utils.parseJsonString(jsonString,AllProductResponse.class);
        assertEquals(response.getMessage(), "Products fetched successfully.");
        List<ProductResponse.ProductData> products= response.getData();
        assertEquals(products.size(),2);
        assertEquals(products.get(0).getName(), "test2");

    }


    @Test
    public void shouldGetProductById() {
        Product prod= addProduct("test1");
        Product prod2= addProduct("test2");
        Product prod3= addProduct("test3");


        String url = ("http://localhost:" + port + "/api/v1/product/"+prod.getId());
        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).when()
                .get(url)
                .then()
                .statusCode(is(200));
        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        ProductResponse response = Utils.parseJsonString(jsonString,ProductResponse.class);
        assertEquals(response.getMessage(), "Product fetched successfully.");
        ProductResponse.ProductData product= response.getData();
        assertEquals(product.getName(), "test1");

    }


    @Test
    public void shouldRefuseIfOwnerDoesOwnProductID() {
        Product prod= addProduct("test1");
        Product prod2= addProduct("test2");
        Product prod3= addProduct("test3");
        Owner owner= new Owner();
        owner.setName("test2");
        owner.setEmail("test2@test.com");
        owner.setPassword("test2");
        owner.setPhoneNumber("25489898989");
        owner.setRole(Role.owner);
        ownerService.save(owner);
        prod.setOwner(owner);
        productRepository.save(prod);

        String url = ("http://localhost:" + port + "/api/v1/product/"+prod.getId());
        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).when()
                .get(url)
                .then()
                .statusCode(is(401));
        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        ProductResponse response = Utils.parseJsonString(jsonString,ProductResponse.class);
        assertEquals(response.getMessage(), "You are not allowed to view this product.");
        ProductResponse.ProductData product= response.getData();
        assertNull(product);

    }

    @Test
    public void shouldDeleteProduct() {
        Product prod= addProduct();
        String url = ("http://localhost:" + port + "/api/v1/product/delete/"+ prod.getId());
        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).when()
                .delete(url)
                .then()
                .statusCode(is(200));

        List<Product> saved = productRepository.findAll();
        assertEquals(0, saved.size());
        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        ProductResponse response = Utils.parseJsonString(jsonString,ProductResponse.class);
        assertEquals(response.getMessage(), "Product deleted successfully.");
        ProductResponse.ProductData product= response.getData();
        assertNull( product);

    }
    @Test
    public void shouldRefuseDeletionIfProductDoesntBelongToOwner() {
        Product prod= addProduct("test1");
        Product prod2= addProduct("test2");
        Product prod3= addProduct("test3");
        Owner owner= new Owner();
        owner.setName("test2");
        owner.setEmail("test2@test.com");
        owner.setPassword("test2");
        owner.setPhoneNumber("25489898989");
        owner.setRole(Role.owner);
        ownerService.save(owner);
        prod.setOwner(owner);
        productRepository.save(prod);
        String url = ("http://localhost:" + port + "/api/v1/product/delete/"+ prod.getId());
        ValidatableResponse res = given().header("Content-Type", "application/json").header("Authorization", token).when()
                .delete(url)
                .then()
                .statusCode(is(401));

        List<Product> saved = productRepository.findAll();
        assertEquals(3, saved.size());
        String jsonString = res.body(containsString("")).extract().response().getBody().asString();
        ProductResponse response = Utils.parseJsonString(jsonString,ProductResponse.class);
        assertEquals(response.getMessage(), "You are not authorized to delete this product.");
        ProductResponse.ProductData product= response.getData();
        assertNull( product);

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
    public static  class AllProductResponse{
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
    public static  class ErrorResponse {

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

