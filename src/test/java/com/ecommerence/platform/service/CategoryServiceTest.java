package com.ecommerence.platform.service;

import com.ecommerence.platform.CategoryAvailableProductsMock;
import com.ecommerence.platform.repository.ProductRepository;
import com.ecommerence.platform.response.CategoryAvailableProducts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(CategoryService.class)
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    public void testGetProductsAvailablePerCategories() throws Exception {


        List<CategoryAvailableProducts> categoryAvailableProducts = new ArrayList<>();
        categoryAvailableProducts.add(new CategoryAvailableProductsMock());
        categoryAvailableProducts.add(new CategoryAvailableProductsMock());

        when(productRepository.findProductsAvailablePerCategories()).thenReturn(categoryAvailableProducts);

        List<CategoryAvailableProducts> categoriesExpected = new ArrayList<>();

        categoriesExpected.add(new CategoryAvailableProductsMock());
        categoriesExpected.add(new CategoryAvailableProductsMock());

        List<CategoryAvailableProducts> result = categoryService.getProductsAvailablePerCategories();

        assertEquals(categoriesExpected.get(0).getCategory(), result.get(0).getCategory());
        assertEquals(categoriesExpected.get(0).getAvailableProducts(), result.get(0).getAvailableProducts());

    }
}
