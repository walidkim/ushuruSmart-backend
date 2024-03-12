package com.emtech.ushurusmart.repository;

<<<<<<< HEAD
import com.emtech.ushurusmart.etrModule.repository.ProductsRepository;
import org.springframework.boot.test.context.SpringBootTest;
import com.emtech.ushurusmart.etrModule.entity.Products;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

=======
import com.emtech.ushurusmart.etrModule.entity.Product;
import com.emtech.ushurusmart.etrModule.repository.ProductsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
>>>>>>> dee7263 (TRansaction Crud)

@SpringBootTest
public class ProductsRepositoryTest {
    @Autowired
    private ProductsRepository productsRepository;
<<<<<<< HEAD
    @Test
    public void saveMethod(){
        Products products = new Products();

        products.setName("Maize");
        products.setDescription("white, whole grain");
        products.setQuantity(90);
        products.setTaxExempted(false);
        products.setUnitOfMeasure("kg");
        products.setUnitPrice(4000);

        Products savedObject=productsRepository.save(products);
    }

=======

    @Test
    public void saveMethod() {
        Product product = new Product();

        product.setName("Maize");
        product.setUnitPrice(9000);
        product.setTaxExempted(false);
        product.setDescription("white grain, mexican origin");
        product.setUnitOfMeasure("kg");
        product.setQuantity(90);

        Product savedObject=productsRepository.save(product);
    }
    @Test
    public void SaveAllMethod(){
        Product product = new Product();

        product.setName("Rice");
        product.setUnitPrice(4500);
        product.setTaxExempted(false);
        product.setDescription("white grain, pakistani origin");
        product.setUnitOfMeasure("kg");
        product.setQuantity(50);

        Product product1 = new Product();

        product1.setName("Beans");
        product1.setUnitPrice(14000);
        product1.setTaxExempted(false);
        product1.setDescription("Yello-green, kenyan origin");
        product1.setUnitOfMeasure("kg");
        product1.setQuantity(90);

        Product product2 = new Product();

        product2.setName("Lentils");
        product2.setUnitPrice(13000);
        product2.setTaxExempted(false);
        product2.setDescription("white whole-grain, indonesian origin");
        product2.setUnitOfMeasure("kg");
        product2.setQuantity(50);

        Product product3 = new Product();

        product3.setName("Sugar");
        product3.setUnitPrice(8000);
        product3.setTaxExempted(false);
        product3.setDescription("white, kenyan origin");
        product3.setUnitOfMeasure("kg");
        product3.setQuantity(50);

        Product product4 = new Product();

        product4.setName("Wheat Flour");
        product4.setUnitPrice(9500);
        product4.setTaxExempted(false);
        product4.setDescription("white-all purpose, russian origin");
        product4.setUnitOfMeasure("kg");
        product4.setQuantity(50);

        productsRepository.saveAll(List.of(product,product1,product2,product3,product4));
    }
    @Test
    public void findByIdMethod(){
        Long id=6L;

        Product product= productsRepository.findById(id).get();
        System.out.println(product.getName());
    }
    @Test
    public void findAllMethod(){

        List<Product>products=productsRepository.findAll();
        products.forEach((p)->{System.out.println(p.getName());});
    }
    @Test
    public void updateMethod(){
        Long id=2L;
        Product product = productsRepository.findById(id).get();

        product.setDescription("white pishori");
        productsRepository.save(product);
    }
    @Test
    public void deleteByIdMethod(){
        Long id=5L;
        productsRepository.deleteById(id);
    }
    @Test
    public void deleteAllByIdMethod(){
        Product product= productsRepository.findById(4L).get();
        Product product1= productsRepository.findById(6L).get();

        productsRepository.deleteAll(List.of(product,product1));
    }
    @Test
    public void deleteAllMethod(){
        productsRepository.deleteAll();
    }
    @Test
    public void countMethod(){
        long count = productsRepository.count();

        System.out.println(count);
    }
>>>>>>> dee7263 (TRansaction Crud)
}






