package com.ecommerence.platform.controller;

import com.ecommerence.platform.dto.CustomerDto;
import com.ecommerence.platform.dto.OrderDto;
import com.ecommerence.platform.service.CustomerService;
import com.ecommerence.platform.service.ICustomerService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class CustomerControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IOrderService orderService;

    @MockBean
    private ICustomerService customerService;


    @Test
    public void testRegisterNewCustomer() throws Exception {

        CustomerDto customerDto = new CustomerDto();
        customerDto.setUsername("johndoe99");
        customerDto.setPassword("passwordJohndoe99");
        customerDto.setFirstName("John");
        customerDto.setLastName("Doe");
        customerDto.setAddress("123 Main St");
        customerDto.setCity("New York");



        when(customerService.register(any(CustomerDto.class))).thenReturn(customerDto);


        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(customerDto);
        String expected = objectMapper.writeValueAsString(customerDto);

        mockMvc.perform(post("/v1/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json(expected));
    }


    @Test
    public void testOrderGlobalSearchForLoggedUser() throws Exception {

        OrderDto orderDto = new OrderDto();
        orderDto.setName("Laptop");
        orderDto.setComment("This is a laptop");
        orderDto.setProductQuantityPairDtoList(new ArrayList<>());


        List<OrderDto> orderDtoList = Arrays.asList(orderDto, orderDto, orderDto);

        when(orderService.orderSearchForLoggedUser(any(String.class)))
                .thenReturn(orderDtoList);

        ObjectMapper objectMapper = new ObjectMapper();
        String expected = objectMapper.writeValueAsString(orderDtoList);

        mockMvc.perform(get("/v1/customers/orders?search=laptop"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }


}
