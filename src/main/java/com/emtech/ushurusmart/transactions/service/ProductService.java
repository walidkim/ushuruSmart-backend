package com.emtech.ushurusmart.transactions.service;

import com.emtech.ushurusmart.transactions.entity.Product;
import com.emtech.ushurusmart.transactions.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product getById(long id) {
        return productRepository.getById(id);
    }

    public Product getByName(String name) {
        return productRepository.getByName(name);
    }

    public Product getByQuantity(int quantity) {
        return productRepository.getByQuantity(quantity);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(long id) {
        productRepository.deleteById(id);
    }
}
