package com.emtech.ushurusmart.Transaction.repository;

import com.emtech.ushurusmart.Transaction.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
<<<<<<< HEAD
@Repository
public interface ProductsRepository extends JpaRepository <Products, Long> {
    Products getByName(String name);

    Products findByProductId(String productId);

    Products getByQuantity(int quantity);
=======



@Repository
public interface ProductsRepository extends JpaRepository <Products, Long> {
    Products getByName(String name);
>>>>>>> b12f94f (changes)
=======
@Repository
public interface ProductsRepository extends JpaRepository <Products, Long> {
    Products getByName(String name);

    Products findByProductId(String productId);

    Products getByQuantity(int quantity);
>>>>>>> 4476ce9 (Credit Note)
}
