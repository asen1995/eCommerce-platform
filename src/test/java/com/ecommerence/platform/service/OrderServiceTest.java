package com.ecommerence.platform.service;

import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.exception.ProductNotFoundException;
import com.ecommerence.platform.exception.ProductQuantityNotEnoughException;
import com.ecommerence.platform.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@WebMvcTest(OrderService.class)
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    public void testOrder5ProductWhenQuantityIs10() throws Exception {
        List<Product> products = new ArrayList<>();

        Product product = new Product();
        product.setId(1);
        product.setName("Dell monitor");
        product.setCategory("Monitor");
        product.setQuantity(10);

        when(productRepository.findByIdForUpdate(anyInt())).thenReturn(Optional.of(product));

        String expected = String.format("You successfully ordered %s %s", 5, product.getName());
        int orderedQuantity = 5;
        String result = orderService.orderProduct(1, orderedQuantity);

        assertEquals(expected, result);
    }

    @Test(expected = ProductQuantityNotEnoughException.class)
    public void testOrder25ProductWhenQuantityIs10() throws Exception {

        Product product = new Product();
        product.setId(1);
        product.setName("Dell monitor");
        product.setCategory("Monitor");
        product.setQuantity(10);

        when(productRepository.findByIdForUpdate(anyInt())).thenReturn(Optional.of(product));

        int orderedQuantity = 25;
        orderService.orderProduct(1, orderedQuantity);

    }

    @Test(expected = ProductNotFoundException.class)
    public void testOrderProductThatDontExists() throws Exception {

        when(productRepository.findByIdForUpdate(anyInt())).thenReturn(Optional.empty());

        int orderedQuantity = 25;
        orderService.orderProduct(1, orderedQuantity);

    }
}
