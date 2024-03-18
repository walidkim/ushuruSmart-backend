package com.emtech.ushurusmart.Transaction.service;

import com.emtech.ushurusmart.Transaction.entity.Products;
import com.emtech.ushurusmart.Transaction.repository.ProductsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {
    private ProductsRepository productsRepository;

    public List<Products> findAll() {
        return productsRepository.findAll();
    }

    public Products getById(long id) {
        return productsRepository.getById(id);
    }

    public Products getByName(String name){return productsRepository.getByName(name);}
<<<<<<< HEAD
<<<<<<< HEAD
    public Products getByQuantity(int quantity){return productsRepository.getByQuantity(quantity);}
=======
>>>>>>> b12f94f (changes)
=======
    public Products getByQuantity(int quantity){return productsRepository.getByQuantity(quantity);}
>>>>>>> 4476ce9 (Credit Note)

    public Products save(Products product) {
        return productsRepository.save(product);
    }

    public void delete(long id) {
         productsRepository.deleteById(id);
    }
<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> b12f94f (changes)
=======

>>>>>>> 4476ce9 (Credit Note)
}
