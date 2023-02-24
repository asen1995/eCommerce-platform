package com.ecommerence.platform.service;

import com.ecommerence.platform.constants.AppConstants;
import com.ecommerence.platform.constants.RsqlConstants;
import com.ecommerence.platform.dto.OrderDto;
import com.ecommerence.platform.dto.ProductQuantityPairDto;
import com.ecommerence.platform.entity.Customer;
import com.ecommerence.platform.entity.Order;
import com.ecommerence.platform.entity.OrderProduct;
import com.ecommerence.platform.entity.Product;
import com.ecommerence.platform.exception.*;
import com.ecommerence.platform.mapper.OrderMapper;
import com.ecommerence.platform.repository.CustomerRepository;
import com.ecommerence.platform.repository.OrderProductRepository;
import com.ecommerence.platform.repository.OrderRepository;
import com.ecommerence.platform.repository.ProductRepository;
import com.ecommerence.platform.response.OrderResponse;
import com.ecommerence.platform.rsql.CustomRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    private final ProductRepository productRepository;

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    public OrderService(ProductRepository productRepository, CustomerRepository customerRepository, OrderRepository orderRepository,
                        OrderProductRepository orderProductRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }


    @Override
    @Transactional
    public OrderResponse orderProduct(Integer id, Integer orderedQuantity) throws Exception {

        logger.info("Ordering product with id: " + id + " and quantity: " + orderedQuantity);

        Optional<Product> oProduct = productRepository.findByIdForUpdate(id);

        if (!oProduct.isPresent()) {
            logger.error("Product with id: " + id + " not found");
            throw new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND_MESSAGE);
        }

        Product product = oProduct.get();

        if (product.getQuantity() >= orderedQuantity) {
            product.setQuantity(product.getQuantity() - orderedQuantity);
            productRepository.save(product);

            logger.info("Product with id: " + id + " successfully ordered");

            return new OrderResponse(String.format(AppConstants.PRODUCT_SUCCESSFUL_ORDER_MESSAGE_TEMPLATE, orderedQuantity, product.getName())
                    , product);

        } else {
            logger.error("Product with id: " + id + " quantity not enough for the order");
            throw new ProductQuantityNotEnoughException(
                    String.format(AppConstants.PRODUCT_QUANTITY_NOT_ENOUGH_MESSAGE_TEMPLATE,
                            orderedQuantity,
                            product.getName(),
                            product.getQuantity()));
        }
    }

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) throws ProductNotFoundException, ProductQuantityNotEnoughException {

        logger.info("Creating order with name : {} ", orderDto.getName());

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Customer customer = customerRepository.findByUsername(username).get();

        Order order = new Order();
        order.setName(orderDto.getName());
        order.setComment(orderDto.getComment());
        order.setCustomer(customer);

        Map<Integer, Product> selectedProductEntitiesMap = productRepository
                .findAllByIdsForUpdate(orderDto.getProductQuantityPairDtoList()
                        .stream()
                        .map(ProductQuantityPairDto::getProductId)
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<OrderProduct> orderedProducts = new ArrayList<>();

        for (ProductQuantityPairDto pair : orderDto.getProductQuantityPairDtoList()) {

            Product product = selectedProductEntitiesMap.get(pair.getProductId());

            if (product == null) {
                logger.error("Product with id: " + pair.getProductId() + " not found");
                throw new ProductNotFoundException(String.format(AppConstants.PRODUCT_WITH_ID_NOT_FOUND_MESSAGE_TEMPLATE, pair.getProductId()));
            }

            if (product.getQuantity() < pair.getQuantity()) {

                logger.error("Product with id: " + pair.getProductId() + " quantity not enough for the order");
                throw new ProductQuantityNotEnoughException(
                        String.format(AppConstants.PRODUCT_QUANTITY_NOT_ENOUGH_MESSAGE_TEMPLATE,
                                pair.getQuantity(),
                                product.getName(),
                                product.getQuantity()));
            }

            product.setQuantity(product.getQuantity() - pair.getQuantity());

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProduct(product);
            orderProduct.setQuantity(pair.getQuantity());
            orderProduct.setOrder(order);

            orderedProducts.add(orderProduct);

        }

        order.setOrderProducts(orderedProducts);
        order.setCreatedDate(new Date());

        orderRepository.save(order);
        orderProductRepository.saveAll(orderedProducts);
        productRepository.saveAll(selectedProductEntitiesMap.values());

        logger.info("Order with name : {} successfully created", orderDto.getName());

        return orderDto;
    }

    @Override
    public List<OrderDto> orderGlobalSearch(String search) {

        logger.info("Searching orders with search string : {} ", search);

        Node rootNode = new RSQLParser().parse(RsqlConstants.ORDER_GLOBAL_SEARCH_RSQL_QUERY.replace("searchString", search));
        Specification<Order> spec = rootNode.accept(new CustomRsqlVisitor<>());

        return orderRepository.findAll(spec).stream().map(order -> {
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

        Node rootNode = new RSQLParser().parse(
                RsqlConstants.ORDER_GLOBAL_SEARCH_FOR_LOGGED_USER_RSQL_QUERY
                        .replace("searchString", search)
                        .replace("loggedUsername", username));

        Specification<Order> spec = rootNode.accept(new CustomRsqlVisitor<>());

        return orderRepository.findAll(spec)
                .stream()
                .map(orderMapper::toOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDto approveOrder(Integer id) throws Exception {

        logger.info("Approving order with id : {} ", id);

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

        logger.info("Order with id : {} successfully approved", id);
        return orderMapper.toOrderDto(order);
    }
}
