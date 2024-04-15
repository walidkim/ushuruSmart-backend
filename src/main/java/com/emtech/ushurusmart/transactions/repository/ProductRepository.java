package com.emtech.ushurusmart.transactions.repository;

import com.emtech.ushurusmart.transactions.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);

    Product getByName(String name);

    Product getByQuantity(int quantity);
    @Query("SELECT p FROM Product p WHERE p.owner.id = :ownerId")
    List<Product> findByOwnerId(@Param("ownerId")long id);

}
