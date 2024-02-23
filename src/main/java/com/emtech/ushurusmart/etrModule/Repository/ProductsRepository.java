package com.emtech.ushurusmart.etrModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emtech.ushurusmart.etrModule.entity.Product;

@Repository

public interface ProductsRepository extends JpaRepository <Product, Integer> {
    Product findByName(String name);
}
