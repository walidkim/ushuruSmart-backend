package com.emtech.ushurusmart.etrModule.service;

import com.emtech.ushurusmart.etrModule.entity.Product;
import com.emtech.ushurusmart.etrModule.repository.ProductsRepository;
import com.emtech.ushurusmart.utils.model.EntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {
    @Autowired
    private ProductsRepository productsRepository;

    public EntityResponse<List<Product>> findAll() {
        List<Product> products = productsRepository.findAll();
        return new EntityResponse<>(HttpStatus.OK.value(),"fetched successfully", products);
    }

    public Product getById(long id) {
        return productsRepository.getById(id);
    }

    public Product getByName(String name) {
        return productsRepository.findByName(name);
    }

    public Product save(Product product) {
        return productsRepository.save(product);
    }

    public void delete(long id) {
         productsRepository.deleteById(id);
    }
}
