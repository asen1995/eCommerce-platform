package com.ecommerence.platform.controller;

import com.ecommerence.platform.constants.AppConstants;
import com.ecommerence.platform.dto.OrderDto;
import com.ecommerence.platform.dto.ProductQuantityPairDto;
import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.exception.ProductNotFoundException;
import com.ecommerence.platform.exception.ProductQuantityNotEnoughException;
import com.ecommerence.platform.response.OrderResponse;
import com.ecommerence.platform.service.IOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IOrderService orderService;

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


    @Test
    public void testCreateOrder() throws Exception {

        OrderDto orderDto = new OrderDto();
        orderDto.setName("Laptop");

        orderDto.setComment("This is a laptop comment for order");
        ProductQuantityPairDto productQuantityPairDto = new ProductQuantityPairDto();
        productQuantityPairDto.setProductId(1);
        productQuantityPairDto.setQuantity(10);

        orderDto.setProductQuantityPairDtoList(Arrays.asList(productQuantityPairDto));

        when(orderService.createOrder(any(OrderDto.class))).thenReturn(orderDto);


        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(orderDto);

        String expected = objectMapper.writeValueAsString(orderDto);

        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().json(expected));
    }


    @Test
    public void testOrderGlobalSearch() throws Exception {

        OrderDto orderDto = new OrderDto();
        orderDto.setName("Laptop");
        orderDto.setComment("This is a laptop");
        orderDto.setProductQuantityPairDtoList(new ArrayList<>());


        List<OrderDto> orderDtoList = Arrays.asList(orderDto, orderDto, orderDto);

        when(orderService.orderGlobalSearch(any(String.class)))
                .thenReturn(orderDtoList);

        ObjectMapper objectMapper = new ObjectMapper();
        String expected = objectMapper.writeValueAsString(orderDtoList);

        mockMvc.perform(get("/v1/orders?search=laptop"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }


    @Test
    public void testApproveOrder() throws Exception {

        OrderDto orderDto = new OrderDto();
        orderDto.setName("Laptop");
        orderDto.setComment("This is a laptop");

        when(orderService.approveOrder(anyInt())).thenReturn(orderDto);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(orderDto);

        mockMvc.perform(put("/v1/orders/approve/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(json));
    }
}

