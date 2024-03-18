package com.emtech.ushurusmart.etrModule.repository;

import com.emtech.ushurusmart.etrModule.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ProductsRepository extends JpaRepository <Product, Long> {
    Product findByName(String name);
}
