package com.ecommerence.platform.service;

import com.ecommerence.platform.response.CategoryAvailableProducts;

import java.util.List;

public interface ICategoryService {
    List<CategoryAvailableProducts> getProductsAvailablePerCategories();
}
