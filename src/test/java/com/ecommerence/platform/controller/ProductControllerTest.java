package com.ecommerence.platform.controller;

import com.ecommerence.platform.constants.AppConstants;
import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.enums.DirectionEnum;
import com.ecommerence.platform.enums.ProductOrderEnum;
import com.ecommerence.platform.response.ProductsResponse;
import com.ecommerence.platform.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Product product;

    @Before
    public void setUp() {
        product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setCategory("Monitor");
        product.setDescription("Monitor for testing");
        product.setQuantity(4);
    }



    @Test
    public void testGetAllProducts() throws Exception {
        Product product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setCategory("Monitor");
        product.setDescription("Monitor for testing");
        product.setQuantity(4);

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Test Product 2");
        product2.setCategory("Monitor");
        product2.setDescription("Monitor for testing 2");
        product2.setQuantity(80);

        ProductsResponse productsResponse = new ProductsResponse(Arrays.asList(product,product2), 84l);


        when(productService.getProducts(any(ProductOrderEnum.class), any(DirectionEnum.class), anyInt(),  anyInt()))
                .thenReturn(productsResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String expected = objectMapper.writeValueAsString(productsResponse);

        mockMvc.perform(get("/product?orderBy=NAME&direction=ASC&page=1&pageSize=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    public void testCreateProduct() throws Exception {

        when(productService.createProduct(any(Product.class))).thenReturn("Success");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(product);

        String expectedMessage = String.format(AppConstants.PRODUCT_CREATED_SUCCESSFULLY_MESSAGE, product.getName());

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedMessage));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        when(productService.updateProduct(any(Product.class))).thenReturn("Success");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(product);

        mockMvc.perform(put("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(AppConstants.PRODUCT_UPDATED_SUCCESSFULLY_MESSAGE));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        when(productService.deleteProduct(anyInt())).thenReturn("Success");

        mockMvc.perform(delete("/product?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().string(AppConstants.PRODUCT_DELETED_SUCCESSFULLY_MESSAGE));
    }
}
