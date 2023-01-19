package com.ecommerence.platform.service;

import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.model.Category;
import com.ecommerence.platform.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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
        List<Product> products = new ArrayList<>();

        Product first = new Product();
        first.setId(1);
        first.setName("Dell monitor");
        first.setCategory("Monitor");
        first.setQuantity(10);

        Product second = new Product();
        second.setId(2);
        second.setName("Dell keyboard");
        second.setCategory("Keyboard");
        second.setQuantity(10);

        Product third = new Product();
        third.setId(3);
        third.setName("HP Monitor");
        third.setCategory("Monitor");
        third.setQuantity(30);


        products.add(first);
        products.add(second);
        products.add(third);

        when(productRepository.findAll()).thenReturn(products);

        List<Category> categoriesExpected = new ArrayList<>();
        Category firstCategory = new Category("Monitor", 40);
        Category secondCategory = new Category("Keyboard", 10);

        categoriesExpected.add(firstCategory);
        categoriesExpected.add(secondCategory);

        List<Category> result = categoryService.getProductsAvailablePerCategories();

        assertArrayEquals(categoriesExpected.toArray(), result.toArray());

    }
}
