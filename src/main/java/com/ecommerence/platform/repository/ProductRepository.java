package com.ecommerence.platform.repository;

import com.ecommerence.platform.constants.SqlConstants;
import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.response.CategoryAvailableProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = SqlConstants.GET_PRODUCT_BY_ID_FOR_UPDATE, nativeQuery = true)
    Optional<Product> findByIdForUpdate(@Param("id") Integer id);

    @Query(value = SqlConstants.GET_DISTINCT_CATEGORIES_AND_AVAILABLE_PRODUCTS, nativeQuery = true)
    List<CategoryAvailableProducts> findProductsAvailablePerCategories();
}
