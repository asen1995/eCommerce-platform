package com.ecommerence.platform.repository;

import com.ecommerence.platform.constants.SqlConstants;
import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.response.CategoryAvailableProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query(value = SqlConstants.GET_PRODUCTS_FOR_UPDATE, nativeQuery = true)
    List<Product> findAllByIdsForUpdate(@Param("ids") List<Integer> ids);

    @Modifying
    @Query(value = SqlConstants.UPSERT_PRODUCT_WITH_UNIQUE_NAME_AND_CATEGORY, nativeQuery = true)
    void upsert(Product product);
}
