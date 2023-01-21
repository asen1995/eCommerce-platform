package com.ecommerence.platform.controller;

import com.ecommerence.platform.model.Category;
import com.ecommerence.platform.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping
    public ResponseEntity<List<Category>> getProductsAvailablePerCategories() {
        List<Category> categoriesWithProductsAvailableList = categoryService.getProductsAvailablePerCategories();

        return new ResponseEntity<>(categoriesWithProductsAvailableList, HttpStatus.OK);
    }


}
