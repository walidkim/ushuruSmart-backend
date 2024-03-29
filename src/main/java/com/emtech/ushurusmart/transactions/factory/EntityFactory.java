package com.emtech.ushurusmart.transactions.factory;

import com.emtech.ushurusmart.transactions.entity.Product;
import com.emtech.ushurusmart.usermanagement.Dtos.entity.ProductDto;

public class EntityFactory {
    public static Product createProduct(ProductDto prod){
        Product product= new Product();
        product.setDescription(prod.getDescription());
        product.setName(prod.getName());
        product.setType(prod.getType());
        product.setUnitOfMeasure(prod.getUnitOfMeasure());
        product.setTaxExempted(prod.isTaxExempted());
        product.setQuantity(prod.getQuantity());
        product.setUnitPrice(prod.getUnitPrice());
        return product;
    }
}
