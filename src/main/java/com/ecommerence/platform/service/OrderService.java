package com.ecommerence.platform.service;

import com.ecommerence.platform.constants.AppConstants;
import com.ecommerence.platform.dto.OrderDto;
import com.ecommerence.platform.dto.ProductQuantityPairDto;
import com.ecommerence.platform.entity.Customer;
import com.ecommerence.platform.entity.Order;
import com.ecommerence.platform.entity.OrderProduct;
import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.exception.*;
import com.ecommerence.platform.repository.CustomerRepository;
import com.ecommerence.platform.repository.OrderProductRepository;
import com.ecommerence.platform.repository.OrderRepository;
import com.ecommerence.platform.repository.ProductRepository;
import com.ecommerence.platform.response.OrderResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    private final ProductRepository productRepository;

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderService(ProductRepository productRepository, CustomerRepository customerRepository, OrderRepository orderRepository,
                        OrderProductRepository orderProductRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    //SERIALIZABLE causes the second waiting transaction to instant fail in Oracle db once the first transaction commit
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
                            , product);

            return orderResponse;

        } else {
            throw new ProductQuantityNotEnoughException(
                    String.format(AppConstants.PRODUCT_QUANTITY_NOT_ENOUGH_MESSAGE_TEMPLATE,
                            orderedQuantity,
                            product.getName(),
                            product.getQuantity()));
        }
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) throws ProductNotFoundException, ProductQuantityNotEnoughException {

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Customer customer = customerRepository.findByUsername(username).get();

        Order order = new Order();
        order.setName(orderDto.getName());
        order.setComment(orderDto.getComment());
        order.setCustomer(customer);

        List<OrderProduct> products = new ArrayList<>();
        for (ProductQuantityPairDto pair : orderDto.getProductQuantityPairDtoList()) {

            Product product = productRepository.findById(pair.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(String.format(AppConstants.PRODUCT_WITH_ID_NOT_FOUND_MESSAGE_TEMPLATE, pair.getProductId())));

            if (product.getQuantity() < pair.getQuantity()) {
                throw new ProductQuantityNotEnoughException(
                        String.format(AppConstants.PRODUCT_QUANTITY_NOT_ENOUGH_MESSAGE_TEMPLATE,
                                pair.getQuantity(),
                                product.getName(),
                                product.getQuantity()));
            }

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProduct(product);
            orderProduct.setQuantity(pair.getQuantity());
            orderProduct.setOrder(order);

            products.add(orderProduct);

        }

        order.setOrderProducts(products);
        order.setCreatedDate(new Date());

        orderRepository.save(order);
        orderProductRepository.saveAll(products);

        return orderDto;
    }

    @Override
    public List<OrderDto> orderGlobalSearch(String search) {
        return orderRepository.findOrdersGloballyContainingSearchString(search).get().stream().map(order -> {
            OrderDto orderDto = new OrderDto();
            orderDto.setName(order.getName());
            orderDto.setComment(order.getComment());

            orderDto.setProductQuantityPairDtoList(
                    order.getOrderProducts().stream().map(
                            orderProduct -> {
                                ProductQuantityPairDto pairDto = new ProductQuantityPairDto();
                                pairDto.setProductId(orderProduct.getProduct().getId());
                                pairDto.setQuantity(orderProduct.getQuantity());
                                return pairDto;
                            }
                    ).collect(Collectors.toList()));

            return orderDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> orderSearchForLoggedUser(String search) {

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return orderRepository.findOrdersContainingSearchStringForLoggedUser(search, username).get().stream().map(order -> {
            OrderDto orderDto = new OrderDto();
            orderDto.setName(order.getName());
            orderDto.setComment(order.getComment());
            return orderDto;
        }).collect(Collectors.toList());

    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public OrderDto approveOrder(Integer id) throws Exception {

        Order order = orderRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND_MESSAGE));


        if (Boolean.FALSE.equals(order.getApproved()))
            throw new OrderHaveAlreadyBeenDeclinedException(String.format(AppConstants.ORDER_WITH_ID_HAVE_ALREADY_BEEN_DECLINED_MESSAGE_TEMPLATE, id));

        if (Boolean.TRUE.equals(order.getApproved()))
            throw new OrderHaveAlreadyBeenApprovedException(String.format(AppConstants.ORDER_WITH_ID_HAVE_ALREADY_BEEN_APPROVED_MESSAGE_TEMPLATE, id));

        //check if createdDate is older than 10 minutes
        if (order.getCreatedDate().getTime() < System.currentTimeMillis() - 10 * 60 * 1000)
            throw new OrderHaveAlreadyBeenDeclinedException(String.format(AppConstants.ORDER_WITH_ID_HAVE_ALREADY_BEEN_DECLINED_MESSAGE_TEMPLATE, id));

        order.setApproved(true);
        orderRepository.save(order);

        OrderDto orderDto = new OrderDto();
        orderDto.setName(order.getName());
        orderDto.setComment(order.getComment());

        return orderDto;
    }
}
