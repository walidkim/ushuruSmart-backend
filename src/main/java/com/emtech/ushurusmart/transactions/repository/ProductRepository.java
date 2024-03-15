package com.emtech.ushurusmart.transactions.repository;

import com.emtech.ushurusmart.transactions.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ProductRepository extends JpaRepository <Product, Long> {
    Product findByName(String name);

    Product findByProductId(String productId);

    Product getByName(String name);

    Product getByQuantity(int quantity);
}
