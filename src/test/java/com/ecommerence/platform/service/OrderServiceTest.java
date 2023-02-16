package com.ecommerence.platform.service;

import com.ecommerence.platform.constants.AppConstants;
import com.ecommerence.platform.dto.OrderDto;
import com.ecommerence.platform.entity.Order;
import com.ecommerence.platform.entity.OrderProduct;
import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.exception.*;
import com.ecommerence.platform.repository.CustomerRepository;
import com.ecommerence.platform.repository.OrderProductRepository;
import com.ecommerence.platform.repository.OrderRepository;
import com.ecommerence.platform.repository.ProductRepository;
import com.ecommerence.platform.response.OrderResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@WebMvcTest(OrderService.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class OrderServiceTest {
    @Autowired
    private IOrderService orderService;
    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderProductRepository orderProductRepository;


    @Test
    public void testOrder5ProductWhenQuantityIs10() throws Exception {

        Product product = new Product();
        product.setId(1);
        product.setName("Dell monitor");
        product.setCategory("Monitor");
        product.setQuantity(10);

        when(productRepository.findByIdForUpdate(anyInt())).thenReturn(Optional.of(product));

        int orderedQuantity = 5;

        String expected = String.format(AppConstants.PRODUCT_SUCCESSFUL_ORDER_MESSAGE_TEMPLATE, 5, product.getName());

        OrderResponse orderResponse = orderService.orderProduct(1, orderedQuantity);

        assertEquals(expected, orderResponse.getMessage());
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
//TODO mock SecurityContextHolder to test createOrder method
//    @Test
//    public void testCreateOrder() throws Exception {
//
//        OrderDto orderDto = new OrderDto();
//        orderDto.setName("Order 1");
//        orderDto.setComment("Comment 1");
//
//        ProductQuantityPairDto productQuantityPairDto = new ProductQuantityPairDto();
//        productQuantityPairDto.setProductId(1);
//        productQuantityPairDto.setQuantity(5);
//
//        orderDto.setProductQuantityPairDtoList(Arrays.asList(productQuantityPairDto));
//
//        Customer customer = new Customer();
//        customer.setId(1);
//        customer.setUsername("user1");
//        customer.setPassword("password1");
//
//        Product product = new Product();
//        product.setId(1);
//        product.setQuantity(10);
//
//
//        when(customerRepository.findByUsername(anyString())).thenReturn(Optional.of(customer));
//
//        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
//
//        OrderDto resultOrderDto = orderService.createOrder(orderDto);
//
//        assertEquals(orderDto.getName(), resultOrderDto.getName());
//        assertEquals(orderDto.getComment(), resultOrderDto.getComment());
//
//    }


    @Test
    public void testOrderGlobalSearch() {

        Order order = new Order();
        order.setName("Order 1");
        order.setComment("Comment 1");


        Product product = new Product();
        product.setId(1);

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setQuantity(5);

        order.setOrderProducts(Arrays.asList(orderProduct));

        when(orderRepository.findAll((Specification) ArgumentMatchers.any())).thenReturn(Arrays.asList(order));
        List<OrderDto> ordersResponse = orderService.orderGlobalSearch("laptop");

        assertEquals(ordersResponse.get(0).getName(), order.getName());
        assertEquals(ordersResponse.get(0).getComment(), order.getComment());
        assertEquals(ordersResponse.get(0).getProductQuantityPairDtoList().get(0).getQuantity(), 5);

    }

    @Test
    public void testSuccesfullApproveOrder() throws Exception {

        Order order = new Order();
        order.setId(1);
        order.setName("Order 1");
        order.setComment("Comment 1");

        //created date 5 minute in the past
        order.setCreatedDate(new Date(System.currentTimeMillis() - 5 * 60 * 1000));

        when(orderRepository.findByIdForUpdate(anyInt())).thenReturn(Optional.of(order));


        OrderDto orderDto = orderService.approveOrder(1);

        assertEquals(orderDto.getName(), order.getName());
        assertEquals(orderDto.getComment(), order.getComment());

    }

    @Test(expected = OrderNotFoundException.class)
    public void testApproveOrderThatDontExist() throws Exception {
        when(orderRepository.findByIdForUpdate(anyInt())).thenReturn(Optional.empty());
        orderService.approveOrder(1);
    }

    @Test(expected = OrderHaveAlreadyBeenDeclinedException.class)
    public void testApproveOrderThatWasDeclined() throws Exception {

        Order order = new Order();
        order.setId(1);
        order.setName("Order 1");
        order.setComment("Comment 1");

        order.setApproved(false);

        when(orderRepository.findByIdForUpdate(anyInt())).thenReturn(Optional.of(order));
        orderService.approveOrder(1);

    }

    @Test(expected = OrderHaveAlreadyBeenApprovedException.class)
    public void testApproveOrderThatWasAlreadyApproved() throws Exception {

        Order order = new Order();
        order.setId(1);
        order.setName("Order 1");
        order.setComment("Comment 1");

        order.setApproved(true);

        when(orderRepository.findByIdForUpdate(anyInt())).thenReturn(Optional.of(order));
        orderService.approveOrder(1);

    }

    @Test(expected = OrderHaveAlreadyBeenDeclinedException.class)
    public void testApproveOrderThatIsOlderThen10Minutes() throws Exception {

        Order order = new Order();
        order.setId(1);
        order.setName("Order 1");
        order.setComment("Comment 1");

        //created date 11 minute in the past
        order.setCreatedDate(new Date(System.currentTimeMillis() - 11 * 60 * 1000));


        when(orderRepository.findByIdForUpdate(anyInt())).thenReturn(Optional.of(order));
        orderService.approveOrder(1);

    }

}
