package com.emtech.ushurusmart.transactions.controller;

import java.util.List;
import com.emtech.ushurusmart.transactions.service.ProductService;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emtech.ushurusmart.transactions.entity.Product;

@RestController
@RequestMapping("/api/tax/Product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private OwnerRepository ownerRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProduct(){
        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id){
        Product product = productService.getById(id);
        if (product == null) {
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Product>(HttpStatus.OK);

    }
    @GetMapping("/name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name){
        Product product = productService.getByName(name);
        if (product == null) {
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }
    @PostMapping("/save")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(ownerRepository.findById(owner.getId()).isEmpty()){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        productService.save(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product product){
        Owner owner = (Owner)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!ownerRepository.findById(owner.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Product currentProduct = productService.getById(id);
        if (currentProduct == null){
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }
        productService.save(product);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!ownerRepository.findById(owner.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        productService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
