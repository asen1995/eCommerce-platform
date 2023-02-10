package com.ecommerence.platform.controller;

import com.ecommerence.platform.constants.AppConstants;
import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.exception.ProductNotFoundException;
import com.ecommerence.platform.exception.ProductQuantityNotEnoughException;
import com.ecommerence.platform.response.OrderResponse;
import com.ecommerence.platform.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations="classpath:application-test.properties")
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void testOrderProduct() throws Exception {

        Product product = new Product();
        product.setId(1);
        product.setName("Dell monitor");
        product.setCategory("Monitor");
        product.setQuantity(10);
        product.setDescription("Dell monitor 24 inch");

        int orderQuantity = 2;

        when(orderService.orderProduct(anyInt(), anyInt())).thenReturn(
                new OrderResponse(String.format(AppConstants.PRODUCT_SUCCESSFUL_ORDER_MESSAGE_TEMPLATE, orderQuantity, product.getName())
                ,product));


        ObjectMapper objectMapper = new ObjectMapper();
        String expected = objectMapper.writeValueAsString(new OrderResponse(String.format(AppConstants.PRODUCT_SUCCESSFUL_ORDER_MESSAGE_TEMPLATE, orderQuantity, product.getName())
                ,product));

        mockMvc.perform(post("/v1/orders/1/order/2"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }


    @Test
    public void testOrderProduct_throwProductNotFound() throws Exception {
        when(orderService.orderProduct(anyInt(), anyInt())).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(post("/v1/orders/1/order/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testOrderProduct_throwProductQuantityNotEnoughException() throws Exception {
        when(orderService.orderProduct(anyInt(), anyInt())).thenThrow(ProductQuantityNotEnoughException.class);

        mockMvc.perform(post("/v1/orders/1/order/2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testOrderProduct_WithNegativeQuantity() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String expected = objectMapper.writeValueAsString(new OrderResponse(AppConstants.QTY_MUST_BE_GREATER_THAN_ZERO_MESSAGE, null));

        mockMvc.perform(post("/v1/orders/1/order/-2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expected));
    }
}

