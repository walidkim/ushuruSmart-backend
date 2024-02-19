package com.emtech.ushurusmart.etrModule.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emtech.ushurusmart.etrModule.entity.Product;
import com.emtech.ushurusmart.etrModule.service.ProductService;

@RestController
@RequestMapping("/api/tax/Product")

public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping
    public List<Product> getAllProduct(){
        return service.listAll();
    }

    @GetMapping("/id")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id){
        Product product = service.getById(id);
        if (product == null) {
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Product>(HttpStatus.OK);

    }
    @GetMapping("/name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name){
        Product product = service.getByName(name);
        if (product == null) {
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }
    @PostMapping("/save")
    public void addProduct(@RequestBody Product product){
        service.save(product);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product product){
        Product currentProduct = service.getById(id);
        if (currentProduct == null){
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }
        service.save(product);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable int id){
        service.delete(id);
    }
}
