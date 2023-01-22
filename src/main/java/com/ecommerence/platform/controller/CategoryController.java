package com.ecommerence.platform.controller;

import com.ecommerence.platform.response.CategoryAvailableProducts;
import com.ecommerence.platform.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/products/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping
    public ResponseEntity<List<CategoryAvailableProducts>> getProductsAvailablePerCategories() {
        List<CategoryAvailableProducts> categoriesWithProductsAvailableList = categoryService.getProductsAvailablePerCategories();

        return new ResponseEntity<>(categoriesWithProductsAvailableList, HttpStatus.OK);
    }


}
