package com.emtech.ushurusmart.etrModule.controller;

import com.emtech.ushurusmart.etrModule.entity.Product;
import com.emtech.ushurusmart.etrModule.service.ProductsService;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tax/Product")
@Tag(name = "Product")

public class ProductController {
    @Autowired
    private ProductsService productsService;

    @Autowired
    private OwnerRepository ownerRepository;

    @GetMapping
    public ResponseEntity<?> getAllProduct(){
        return ResponseEntity.ok(productsService.findAll());
    }

    @GetMapping("/id")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id){
        Product product = productsService.getById(id);
        if (product == null) {
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Product>(HttpStatus.OK);

    }
    @GetMapping("/name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name){
        Product product = productsService.getByName(name);
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
        productsService.save(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product product){
        Owner owner = (Owner)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!ownerRepository.findById(owner.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Product currentProduct = productsService.getById(id);
        if (currentProduct == null){
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }
        productsService.save(product);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!ownerRepository.findById(owner.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        productsService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
