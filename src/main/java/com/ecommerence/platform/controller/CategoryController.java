package com.ecommerence.platform.controller;

import com.ecommerence.platform.response.CategoryAvailableProducts;
import com.ecommerence.platform.service.ICategoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/products/categories")
public class CategoryController {


    private static final Logger logger = LogManager.getLogger(CategoryController.class);

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping
    public ResponseEntity<List<CategoryAvailableProducts>> getProductsAvailablePerCategories() {

        List<CategoryAvailableProducts> categoriesWithProductsAvailableList = categoryService.getProductsAvailablePerCategories();

        logger.info("Returning list of categories with products available: {}", categoriesWithProductsAvailableList);
        return new ResponseEntity<>(categoriesWithProductsAvailableList, HttpStatus.OK);
    }


}
