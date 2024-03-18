package com.emtech.ushurusmart.repository;

import com.emtech.ushurusmart.etrModule.repository.ProductsRepository;
import org.springframework.boot.test.context.SpringBootTest;
import com.emtech.ushurusmart.etrModule.entity.Products;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


@SpringBootTest
public class ProductsRepositoryTest {
    @Autowired
    private ProductsRepository productsRepository;
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

}
