package com.ecommerence.platform.service;

import com.ecommerence.platform.response.CategoryAvailableProducts;
import com.ecommerence.platform.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final ProductRepository productRepository;

    public CategoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<CategoryAvailableProducts> getProductsAvailablePerCategories() {
        return productRepository.findProductsAvailablePerCategories();
    }
}
