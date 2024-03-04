package com.emtech.ushurusmart.etrModule.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emtech.ushurusmart.etrModule.entity.Product;
import com.emtech.ushurusmart.etrModule.Repository.ProductsRepository;

@Service

public class ProductsService {

    @Autowired
    private ProductsRepository pRepository;

    public List<Product> listAll() {
        List<Product> list = new ArrayList<>();
        pRepository.findAll().forEach(list::add);
        return list;
    }

    @SuppressWarnings("null")
    public void save(Product product) {
        pRepository.save(product);
    }

    @SuppressWarnings("null")
    public Product getById(Integer id) {
        return pRepository.findById(id).get();
    }

    public Product getByName(String name) {
        return pRepository.findByName(name);
    }

    @SuppressWarnings("null")
    public void delete(Integer id) {
        pRepository.deleteById(id);
    }

}
