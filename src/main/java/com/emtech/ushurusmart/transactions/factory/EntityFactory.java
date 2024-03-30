package com.emtech.ushurusmart.transactions.factory;

import com.emtech.ushurusmart.transactions.entity.Product;
import com.emtech.ushurusmart.usermanagement.Dtos.entity.ProductDto;

public class EntityFactory {
    public static Product createProduct(ProductDto prod){
        Product product= new Product();
        product.setDescription(prod.getDescription());
        product.setName(prod.getName());
        product.setTaxable(prod.isTaxable());
        product.setUnitOfMeasure(prod.getUnitOfMeasure());
        product.setQuantity(prod.getQuantity());
        product.setUnitPrice(prod.getUnitPrice());
        return product;
    }

    public static Product updateProduct(Product original, ProductDto prod){
        original.setDescription(prod.getDescription());
        original.setName(prod.getName());
        original.setTaxable(prod.isTaxable());
        original.setUnitOfMeasure(prod.getUnitOfMeasure());
        original.setQuantity(prod.getQuantity());
        original.setUnitPrice(prod.getUnitPrice());
        return original;
    }
}
