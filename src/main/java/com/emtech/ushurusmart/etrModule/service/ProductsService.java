package com.emtech.ushurusmart.etrModule.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emtech.ushurusmart.etrModule.Repository.ProductRepository;
import com.emtech.ushurusmart.etrModule.entity.Product;


@Service


public class ProductService {

    @Autowired
    private ProductRepository pRepo;

    public List<Product> listAll() {
        List<Product> list = new ArrayList<>();
        pRepo.findAll().forEach(list::add);
        return list;
    }

    @SuppressWarnings("null")
    public void save(Product product){
        pRepo.save(product);
    }
    @SuppressWarnings("null")
    public Product getById(Integer id) {
		return pRepo.findById(id).get();       
	}

    public Product getByName(String name){
        return pRepo.findByName(name);
    }
    @SuppressWarnings("null")
    public void delete(Integer id){
        pRepo.deleteById(id);
    }
	
}
