package com.emtech.ushurusmart.transactions.controller;

import java.util.List;

import com.emtech.ushurusmart.transactions.entity.Product;
import com.emtech.ushurusmart.transactions.service.ProductService;
import com.emtech.ushurusmart.usermanagement.Dtos.ResContructor;
import com.emtech.ushurusmart.usermanagement.Dtos.entity.ProductDto;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tax")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OwnerRepository ownerRepository;

    @GetMapping("/products")
    public ResponseEntity<ResContructor> getAllProduct(){
        ResContructor res= new ResContructor();
        res.setData(productService.findAll());
        return  ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id){
        ResContructor res= new ResContructor();
       try {
           Product product = productService.getById(id);
           if (product == null) {
               res.setMessage("No product with that Id exists");
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
           }

           res.setData(product);

           return ResponseEntity.status(HttpStatus.OK).body(" THis "+ res.toString());
       }catch (Exception e){
           res.setMessage(e.getLocalizedMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
       }

    }
    @GetMapping("/product")
    public ResponseEntity<Product> getProductByName(@RequestParam(name = "name", required = true) String name){
        Product product = productService.getByName(name);
        if (product == null) {
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }
    @PostMapping("product/create")
    public ResponseEntity<Product> addProduct(@RequestBody ProductDto prod){
        Product product= new Product();
        product.setDescription(prod.getDescription());
        product.setName(prod.getName());
        product.setType(prod.getType());
        product.setUnitOfMeasure(prod.getUnitOfMeasure());
        product.setTaxExempted(prod.isTaxExempted());
        product.setQuantity(prod.getQuantity());
        product.setUnitPrice(prod.getUnitPrice());
        productService.save(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
    @PutMapping("product/update/{id}")
    public ResponseEntity<ResContructor> updateProduct(@PathVariable Integer id, @RequestBody ProductDto prod){
        ResContructor res= new ResContructor();
        Product currentProduct = productService.getById(id);
        if (currentProduct == null){
            res.setMessage("No product with that id exists.");
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }

        currentProduct.setDescription(prod.getDescription());
        currentProduct.setName(prod.getName());
        currentProduct.setType(prod.getType());
        currentProduct.setUnitOfMeasure(prod.getUnitOfMeasure());
        currentProduct.setTaxExempted(prod.isTaxExempted());
        currentProduct.setQuantity(prod.getQuantity());
        currentProduct.setUnitPrice(prod.getUnitPrice());
        currentProduct= productService.save(currentProduct);
        res.setMessage("Product updated successfully.");
        res.setData(currentProduct);
        return  ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @DeleteMapping("product/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        ResContructor res= new ResContructor();
        productService.delete(id);
        res.setMessage("Product successfully deleted.");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
