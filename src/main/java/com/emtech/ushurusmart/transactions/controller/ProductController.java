package com.emtech.ushurusmart.transactions.controller;

import com.emtech.ushurusmart.transactions.entity.Product;
import com.emtech.ushurusmart.transactions.factory.EntityFactory;
import com.emtech.ushurusmart.transactions.service.ProductService;
import com.emtech.ushurusmart.usermanagement.Dtos.entity.ProductDto;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import com.emtech.ushurusmart.usermanagement.service.AssistantService;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.usermanagement.utils.AuthUtils;
import com.emtech.ushurusmart.utils.controller.ResContructor;
import com.emtech.ushurusmart.utils.controller.Responses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OwnerRepository ownerRepository;


    @Autowired
    private OwnerService ownerService;

    @Autowired
    private AssistantService assistantService;

    @Autowired
    private Responses responses;

    @GetMapping("/products")
    public ResponseEntity<ResContructor> getAllProduct(){
        try {
            ResContructor res= new ResContructor();

            String email= AuthUtils.getCurrentlyLoggedInPerson();
            Owner owner= ownerService.findByEmail(email);
            if(owner==null){
                owner= assistantService.findByEmail(email).getOwner();
            }
            res.setData(productService.findAllByOwnerId(owner.getId()));
            res.setMessage("Products fetched successfully.");
            return  ResponseEntity.status(HttpStatus.OK).body(res);
        }catch (Exception e){
            return responses.create500Response(e);
        }
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

           String email= AuthUtils.getCurrentlyLoggedInPerson();
           Owner owner= ownerService.findByEmail(email);
           if(owner==null){
               owner= assistantService.findByEmail(email).getOwner();
           }
           long ownerId = owner.getId();
           if(ownerId != product.getOwner().getId()){
               res.setMessage("You are not allowed to view this product.");
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
           }
           res.setMessage("Product fetched successfully.");
           res.setData(product);
           return ResponseEntity.status(HttpStatus.OK).body(res);
       }catch (Exception e){
         return responses.create500Response(e);
       }

    }
    @GetMapping("/product")
    public ResponseEntity<ResContructor> getProductByName(@RequestParam(name = "name", required = true) String name){
        ResContructor res= new ResContructor();
        String email= AuthUtils.getCurrentlyLoggedInPerson();
        Owner owner= ownerService.findByEmail(email);
        if(owner==null){
            owner= assistantService.findByEmail(email).getOwner();
        }

        Product product = productService.getByName(name);

        if(owner.getId() != product.getOwner().getId()){
             res.setMessage("You don't own this product hence you can't view it.");
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
        if (product == null) {
            res.setMessage("No product by  that name exists");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);

        }
        res.setMessage("Product fetched successfully.");
        res.setData(product);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @PostMapping("product/create")
    public ResponseEntity<ResContructor> addProduct(@RequestBody ProductDto prod){
        ResContructor res= new ResContructor();
        try {
            String email= AuthUtils.getCurrentlyLoggedInPerson();
            Owner owner= ownerService.findByEmail(email);
            if(owner==null){
                owner= assistantService.findByEmail(email).getOwner();
            }
            Product product= EntityFactory.createProduct(prod);
            product.setOwner(owner);
            res.setMessage("Product created successfully!");
            res.setData(productService.save(product));
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }catch (Exception e){
            return responses.create500Response(e);
        }
    }
    @PutMapping("product/update/{id}")
    public ResponseEntity<ResContructor> updateProduct(@PathVariable long id, @RequestBody ProductDto data){
        ResContructor res= new ResContructor();
       try {
           Product currentProduct = productService.getById(id);
           String email= AuthUtils.getCurrentlyLoggedInPerson();
           Owner owner= ownerService.findByEmail(email);
           if(owner==null){
               owner= assistantService.findByEmail(email).getOwner();
           }
           if (currentProduct == null){
               res.setMessage("No product with that id exists.");
               return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
           }
           if(owner.getId() != currentProduct.getOwner().getId()){
               res.setMessage("You are not authorized to modify this product.");
               return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
           }
           currentProduct= productService.save(EntityFactory.updateProduct(currentProduct,data));
           res.setMessage("Product updated successfully.");
           res.setData(currentProduct);
           return  ResponseEntity.status(HttpStatus.OK).body(res);
       } catch (Exception e){
           return responses.create500Response(e);
       }
    }
    @DeleteMapping("product/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
      try {
          ResContructor res= new ResContructor();
          String email= AuthUtils.getCurrentlyLoggedInPerson();
          Owner owner= ownerService.findByEmail(email);
          if(owner==null){
              owner= assistantService.findByEmail(email).getOwner();
          }
          if(productService.getById(id).getOwner().getId() != owner.getId()){
              res.setMessage("You are not authorized to delete this product.");
              return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
          }

          productService.delete(id);
          res.setMessage("Product deleted successfully.");
          return ResponseEntity.status(HttpStatus.OK).body(res);
      }catch ( Exception e){
          return responses.create500Response(e);
      }
    }
}
