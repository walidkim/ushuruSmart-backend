package com.emtech.ushurusmart.transactions.service;

import com.emtech.ushurusmart.transactions.entity.Product;
import com.emtech.ushurusmart.transactions.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {
    private ProductRepository productsRepository;

    public List<Product> findAll() {
        return productsRepository.findAll();
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
