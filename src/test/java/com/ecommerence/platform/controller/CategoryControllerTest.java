package com.ecommerence.platform.controller;

import com.ecommerence.platform.CategoryAvailableProductsMock;
import com.ecommerence.platform.response.CategoryAvailableProducts;
import com.ecommerence.platform.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void testGetProductsAvailablePerCategories() throws Exception {
        List<CategoryAvailableProducts> categoryAvailableProducts = new ArrayList<>();
        categoryAvailableProducts.add(new CategoryAvailableProductsMock());

        when(categoryService.getProductsAvailablePerCategories()).thenReturn(categoryAvailableProducts);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(categoryAvailableProducts);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/products/categories"))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    public void testGetProductsAvailablePerCategories_with3Categories() throws Exception {
        List<CategoryAvailableProducts> categoryAvailableProducts = new ArrayList<>();
        categoryAvailableProducts.add(new CategoryAvailableProductsMock());
        categoryAvailableProducts.add(new CategoryAvailableProductsMock());
        categoryAvailableProducts.add(new CategoryAvailableProductsMock());

        when(categoryService.getProductsAvailablePerCategories()).thenReturn(categoryAvailableProducts);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(categoryAvailableProducts);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/products/categories"))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }
}

