package com.ecommerence.platform.repository;

import com.ecommerence.platform.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT * FROM product WHERE id = :id FOR UPDATE", nativeQuery = true)
    Optional<Product> findByIdForUpdate(@Param("id") Integer id);

}
