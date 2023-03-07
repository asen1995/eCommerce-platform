package com.ecommerence.platform.controller;

import com.ecommerence.platform.constants.AppConstants;
import com.ecommerence.platform.dto.OrderDto;
import com.ecommerence.platform.response.OrderResponse;
import com.ecommerence.platform.service.IOrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    private static final Logger logger = LogManager.getLogger(OrderController.class);

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/{productId}/order/{quantity}")
    public ResponseEntity<OrderResponse> orderProduct(@PathVariable("productId") Integer productId, @PathVariable("quantity") Integer quantity) throws Exception {

        logger.info("Ordering product with id: {} and quantity: {}", productId, quantity);

        if (quantity <= 0) {
            logger.error("Quantity provided ( {} ) is less than or equal to zero", quantity);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new OrderResponse(AppConstants.QTY_MUST_BE_GREATER_THAN_ZERO_MESSAGE, null));
        }

        OrderResponse orderResponse = orderService.orderProduct(productId, quantity);

        logger.info("Ordering product finished successfully");
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderResponse);
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto order) throws Exception {

        logger.info("Creating order started");
        OrderDto orderDto = orderService.createOrder(order);

        logger.info("Creating order finished successfully");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderDto);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORDER_MANAGER')")
    public ResponseEntity<List<OrderDto>> orderGlobalSearch(@RequestParam(value = "search") String search) {

        logger.info("Searching for orders with search: {}", search);
        List<OrderDto> orderDtoList = orderService.orderGlobalSearch(search);

        logger.info("Searching for orders finished successfully");
        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORDER_MANAGER')")
    public ResponseEntity<OrderDto> approve(@PathVariable("id") Integer id) throws Exception {

        logger.info("Approving order with id: {} started", id);
        OrderDto orderDto = orderService.approveOrder(id);

        logger.info("Approving order with id: {} finished successfully", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderDto);
    }
}
