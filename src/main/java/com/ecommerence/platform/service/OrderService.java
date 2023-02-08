package com.ecommerence.platform.service;

import com.ecommerence.platform.constants.AppConstants;
import com.ecommerence.platform.dto.OrderDto;
import com.ecommerence.platform.entity.Customer;
import com.ecommerence.platform.entity.Order;
import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.exception.CustomerNotFoundException;
import com.ecommerence.platform.exception.ProductNotFoundException;
import com.ecommerence.platform.exception.ProductQuantityNotEnoughException;
import com.ecommerence.platform.repository.CustomerRepository;
import com.ecommerence.platform.repository.OrderRepository;
import com.ecommerence.platform.repository.ProductRepository;
import com.ecommerence.platform.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final ProductRepository productRepository;

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public OrderService(ProductRepository productRepository, CustomerRepository customerRepository, OrderRepository orderRepository
    ) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }


    @Transactional(isolation = Isolation.READ_COMMITTED) //SERIALIZABLE causes the second waiting transaction to instant fail in Oracle db once the first transaction commit
    public OrderResponse orderProduct(Integer id, Integer orderedQuantity) throws Exception {

        Optional<Product> oProduct = productRepository.findByIdForUpdate(id);

        if (!oProduct.isPresent()) {
            throw new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND_MESSAGE);
        }

        Product product = oProduct.get();

        if (product.getQuantity() >= orderedQuantity) {
            product.setQuantity(product.getQuantity() - orderedQuantity);
            productRepository.save(product);

            OrderResponse orderResponse =
                    new OrderResponse(String.format(AppConstants.PRODUCT_SUCCESSFUL_ORDER_MESSAGE_TEMPLATE, orderedQuantity, product.getName())
                    ,product);

            return orderResponse;

        } else {
            throw new ProductQuantityNotEnoughException(
                    String.format(AppConstants.PRODUCT_QUANTITY_NOT_ENOUGH_MESSAGE_TEMPLATE,
                            orderedQuantity,
                            product.getName(),
                            product.getQuantity()));
        }
    }

    public OrderResponse placeOrder(OrderDto orderDto) throws CustomerNotFoundException, ProductNotFoundException {

        Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(AppConstants.CUSTOMER_NOT_FOUND_MESSAGE));


        List<Product> products = new ArrayList<>();

        for (Integer id : orderDto.getProductIds()){
            products.add(productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND_MESSAGE)));
        }

        Order order  = new Order();
        order.setName(orderDto.getName());
        order.setComment(orderDto.getComment());
        order.setCustomer(customer);
        order.setProducts(products);

        orderRepository.save(order);

        return null;
    }
}
