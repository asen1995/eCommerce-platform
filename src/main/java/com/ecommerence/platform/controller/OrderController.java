package com.ecommerence.platform.controller;

import com.ecommerence.platform.constants.AppConstants;
import com.ecommerence.platform.dto.OrderDto;
import com.ecommerence.platform.exception.OrderNotFoundException;
import com.ecommerence.platform.response.OrderResponse;
import com.ecommerence.platform.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/v1/products/{id}/order/{quantity}")
    public ResponseEntity<OrderResponse> orderProduct(@PathVariable("id") Integer id, @PathVariable("quantity") Integer quantity) throws Exception {

        if (quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new OrderResponse(AppConstants.QTY_MUST_BE_GREATER_THAN_ZERO_MESSAGE, null));
        }

        OrderResponse orderResponse = orderService.orderProduct(id, quantity);

        return ResponseEntity.status(HttpStatus.OK)
                .body(orderResponse);
    }

    @PostMapping("/v1/products/order")
    public ResponseEntity<OrderDto> placeOrder(@RequestBody OrderDto order) throws Exception {

        OrderDto orderDto = orderService.placeOrder(order);


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderDto);
    }


    @GetMapping("/v1/products/orders")
    public ResponseEntity<List<OrderDto>> orderGlobalSearch(@RequestParam(value = "search") String search) {

        List<OrderDto> orderDtoList = orderService.orderGlobalSearch(search);

        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    @PutMapping("/v1/products/orders/approve/{id}")
    public ResponseEntity<OrderDto> approve(@PathVariable("id") Integer id) throws OrderNotFoundException {

        OrderDto orderDto = orderService.approveOrder(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(orderDto);
    }
}
