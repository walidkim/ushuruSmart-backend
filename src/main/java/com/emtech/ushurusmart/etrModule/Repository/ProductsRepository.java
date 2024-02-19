package com.emtech.ushurusmart.etrModule.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emtech.ushurusmart.etrModule.entity.products;
@Repository

public interface ProductsRepository extends JpaRepository <products, Integer> {
    products findByName(String name);
}
