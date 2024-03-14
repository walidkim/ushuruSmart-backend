package com.emtech.ushurusmart.Transaction.repository;

import com.emtech.ushurusmart.Transaction.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository <Products, Long> {
    Products getByName(String name);

    Products findByProductId(String productId);

    Products getByQuantity(int quantity);
}
